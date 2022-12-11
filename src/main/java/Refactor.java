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
//        Expression expToExtract = null;
//        for (Expression exp : method.findAll(Expression.class)) {
//            if (exp.getRange().get().equals(rangeToExtract)) {
//                expToExtract = exp;
//                break;
//            }
//        }
//        if (expToExtract == null)
//            return false;
//        int index = 0;
//        for (Statement s : ((BlockStmt)expToExtract.getParentNode().get()).getStatements()) {
//            if (s.equals(expToExtract.getParentNode())) {
//                VariableDeclarationExpr newDec = new VariableDeclarationExpr(newVarType, newVarName);
//                AssignExpr assExp = new AssignExpr(newDec, expToExtract.clone(), AssignExpr.Operator.ASSIGN);
//                method.getBody().get().getStatements().add(index, new ExpressionStmt(assExp));
//                expToExtract.replace(new NameExpr(newVarName));
//                return true;
//            }
//            index += 1;
//        }


//        System.out.println(method.getChildNodes());
        Expression expToExtract = null;
        Statement parentStatement = null;
        for (Statement s : method.getBody().get().getStatements()) {
//        for (BlockStmt s : method.findAll(BlockStmt.class)) {
//            System.out.println(s);
//            System.out.println("###############");
//            System.out.println(s);
            for (Expression exp : s.findAll(Expression.class)) {
                if (exp.getRange().get().equals(rangeToExtract)) {
                    expToExtract = exp;
                    parentStatement = s;
//                    ((Node)expToExtract).ge
//                    System.out.println(exp.getParentNode().get());
//                    VariableDeclarationExpr newDec = new VariableDeclarationExpr(newVarType, newVarName);
////                    System.out.println(newDec);
//                    AssignExpr e = new AssignExpr(newDec, exp.clone(), AssignExpr.Operator.ASSIGN);
////                    System.out.println(e);
//                    method.getBody().get().getStatements().add(index, new ExpressionStmt(e));
//                    exp.replace(new NameExpr(newVarName));
//                    System.out.println(exp);
//                    System.out.println(e);
                    break;
//                    return true;

                }
            }

        }
        if (expToExtract == null)
            return false;
        int index = 0;
        Node curr = expToExtract;
        while (!(curr instanceof Statement)) {
            curr = curr.getParentNode().get();
        }
        System.out.println(curr);
        for (Statement s : ((BlockStmt)curr.getParentNode().get()).getStatements()) {
            if (s.equals(curr)) {
                VariableDeclarationExpr newDec = new VariableDeclarationExpr(newVarType, newVarName);
                AssignExpr e = new AssignExpr(newDec, expToExtract.clone(), AssignExpr.Operator.ASSIGN);
                ((BlockStmt)curr.getParentNode().get()).getStatements().add(index, new ExpressionStmt(e));
                expToExtract.replace(new NameExpr(newVarName));
                return true;
            }
            index += 1;
        }
        return false;
    }

    public static boolean rename(CompilationUnit cu, final Range rangeToRename, final String renameTo) {
        String nodeToChange= null;
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
