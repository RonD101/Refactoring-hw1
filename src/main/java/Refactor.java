import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.declarations.AssociableToAST;
import javassist.Loader;

import javax.naming.Name;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Refactor {
    public static boolean extract(MethodDeclaration method, final Range rangeToExtract, final String newVarName, final Type newVarType) {
        Expression expToExtract = null;
        for (Expression exp : method.findAll(Expression.class)) {
            if (exp.getRange().get().equals(rangeToExtract)) {
                expToExtract = exp;
                break;
            }
        }
        if (expToExtract == null)
            return false;
        Node curr = expToExtract;
        while (!(curr instanceof Statement)) {
            curr = curr.getParentNode().get();
        }
        VariableDeclarationExpr newDec = new VariableDeclarationExpr(newVarType, newVarName);
        AssignExpr assExp = new AssignExpr(newDec, expToExtract.clone(), AssignExpr.Operator.ASSIGN);
        expToExtract.replace(new NameExpr(newVarName));
        Node parentNode = curr.getParentNode().get();
        if (!(parentNode instanceof  BlockStmt)) {
            BlockStmt newBlock = new BlockStmt();
            newBlock.getStatements().add(0, (Statement) curr.clone());
            curr.replace(newBlock);
            curr = newBlock.getStatements().getFirst().get();
        }
        int index = 0;
        for (Statement s : ((BlockStmt)curr.getParentNode().get()).getStatements()) {
            if (s.equals(curr)) {
                ((BlockStmt)curr.getParentNode().get()).getStatements().add(index, new ExpressionStmt(assExp));
                return true;
            }
            index += 1;
        }
        return false;
    }

    public static boolean rename(CompilationUnit cu, final Range rangeToRename, final String renameTo) {
        String nodeToChange = null;
        BlockStmt bStmtToChange = null;
        for (BlockStmt bStmt : cu.findAll(BlockStmt.class)) {
            for (SimpleName sName : bStmt.getParentNode().get().findAll(SimpleName.class)) {
                if (sName.getRange().get().equals(rangeToRename)) {
                    if (Objects.equals(sName.getIdentifier(), renameTo))
                        return false;
                    nodeToChange = sName.getIdentifier();
                    bStmtToChange = bStmt;
                    break;
                }
            }
        }
        if (nodeToChange == null)
            return false;
        for (SimpleName sName : bStmtToChange.getParentNode().get().findAll(SimpleName.class)) {
            if (Objects.equals(sName.getIdentifier(), nodeToChange))
                sName.setIdentifier(renameTo);
        }
        return true;
    }
}
