import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.declarations.AssociableToAST;
import javassist.Loader;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Refactor {
    public static boolean extract(MethodDeclaration method, final Range rangeToExtract, final String newVarName, final Type newVarType) {
        return true;
    }

    public static boolean rename(CompilationUnit cu, final Range rangeToRename, final String renameTo) {
        List<SimpleName> sNameList = cu.findAll(SimpleName.class);
        for (SimpleName sName : sNameList) {
            if (sName.getRange().get().equals(rangeToRename)) {
                if (Objects.equals(sName.getIdentifier(), renameTo))
                    return false;
                sName.setIdentifier(renameTo);
                return true;
            }
        }

//        List<Parameter> expList = cu.findAll(Parameter.class);
//        for (Parameter expName : expList) {
//            if (expName.getRange().get().equals(rangeToRename)) {
//                if (Objects.equals(expName.getName(), renameTo))
//                    return false;
//                expName.setName(renameTo);
//                return true;
//            }
//        }
        return false;
//        RangeFinder rangeFinder = new RangeFinder();
//        cu.accept(rangeFinder, rangeToRename);
//
//        Build string›
//        StringBuilder codeBuilder = new StringBuilder();
//        rangeFinder.nodesFound.forEach(node -> codeBuilder.append(node.toString()).append("\n"));
//        String nodeStr = codeBuilder.toString();
//        System.out.println(nodeStr);
//        System.out.println(cu.getClassByName(nodeStr).isPresent());
//        boolean changed = false;
//        for (Node n: rangeFinder.nodesFound) {
//            ((SimpleName)n).setIdentifier(renameTo);
//            changed = true;
//            VariableDeclarationExpr var = (VariableDeclarationExpr)n;
//            var.remove();
//            codeBuilder.
//            System.out.println("Enter For");
////            NodeWithSimpleName<Node> sNode = NodeWithSimpleName<Node>.cast;
////            sNode.setName(renameTo);
//        }

//        Node n = cu.setRange(rangeToRename);
//        String s = n.toString();
//        Node newNode = n.clone();
//        s = cu.getType(0).getName().asString();
//        System.out.println(s);
//        NodeWithSimpleName<Node> sNode = (NodeWithSimpleName<Node>)newNode;
//        sNode.setName(renameTo);
//        cu.replace(n, (Node)sNode);

//        cu.setTokenRange(TokenRange(rangeToRename.begin,rangeToRename.end));
//        NodeWithRange<Node> temp = new NodeWithRange<Node>();
//        temp.setRange(rangeToRename);
//        cu.replace(renameTo,renameTo);
//        return changed;
    }


}
