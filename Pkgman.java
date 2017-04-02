import java.util.*;

/**
 * Created by berkeyanilmaz on 2017-03-30.
 */
public class Pkgman {
    /*
    *  A HashMap cannot contain duplicates and the search operation is done in constant time.
    *  ArrayList eliminates the resizing issues.
    *  Stack's LIFO structure is helpful with validating dependent packages.
    */
    static HashMap<String, Package> existingPkg = new HashMap<>();
    static HashMap<String, ArrayList<String>> dependencies = new HashMap<>();

    public static void main(String[] args) {

        try (Scanner sc = new Scanner(System.in)) {
            String userInput;
            ArrayList<String> tokens;

            while (true) {
                System.out.print(">> ");
                userInput = sc.nextLine();
                tokens = tokenize(userInput);
                tokens.remove(0);
                String command = tokens.remove(0);
                //Tokens left are package names
                switch (command) {
                    case "add":
                        add(tokens);
                        break;
                    case "list":
                        list();
                        break;
                    case "rem":
                        remove(tokens.get(0));
                        break;
                    default:
                        System.out.println("Invalid command.");

                }
            }
        }
    }

    private static void remove(String pkgName) {
        Package p = existingPkg.get(pkgName);
        if (existingPkg.containsKey(pkgName)) {
            existingPkg.remove(pkgName);
            System.out.println(pkgName + " removed.");
        } else {
            System.out.println("Non-existing package is not removable.");
            System.exit(1);
        }

        for (String dep : p.getDependencies()) {
            if (dependencies.containsKey(dep)) {
                ArrayList<String> newDep = dependencies.get(dep);
                newDep.remove(p.getName());
            }
        }
        validateAll();
    }

    private static void add(ArrayList<String> usrCmd) {
        //No dependencies
        if (usrCmd.size() == 1) {
            Package p = new Package(usrCmd.remove(0), new ArrayList<>());
            p.setValid(true);
            existingPkg.put(p.getName(), p);
            System.out.println(p.getName() + " added, status valid");
        } else {
            Package p = new Package(usrCmd.remove(0), usrCmd);
            existingPkg.put(p.getName(), p);
            for (String str : p.getDependencies()) {

                if (!dependencies.containsKey(str)) {
                    ArrayList<String> tmp = new ArrayList<>();
                    tmp.add(p.getName());
                    dependencies.put(str, tmp);
                } else {
                    ArrayList<String> tmp = dependencies.get(str);
                    tmp.add(p.getName());
                    dependencies.put(str, tmp);
                }
            }
            //Check validity
            p.setValid(validatePkg(p.getName()));
            if (!p.isValid()) {
                System.out.println(p.getName() + " added, status invalid");
            } else {
                System.out.println(p.getName() + " added, status valid");
            }

        }
    }

    private static void list() {
        validateAll();
        for (Map.Entry<String, Package> entry : existingPkg.entrySet()) {
            String key = entry.getKey();
            Package value = entry.getValue();

            System.out.println(key + " - " + value.isValid());
        }
    }

    private static void validateAll() {
        for (String s : existingPkg.keySet()) {
            existingPkg.get(s).setValid(validatePkg(s));
        }
    }

    private static boolean validatePkg(String pkgName) {
        Package p = existingPkg.get(pkgName);
        Stack<String> stk = getDependencyOrder(p);

        while (!stk.isEmpty()) {
            String s = stk.pop();
            if (!existingPkg.containsKey(s)) {
                return false;
            }
        }
        return true;
    }

    private static Stack<String> getDependencyOrder(Package p) {
        //Last-in, first-out
        Stack<String> stk = new Stack<>();
        ArrayList<String> pkgDep = p.getDependencies();

        for (String d : pkgDep) {
            if (!pkgDep.isEmpty()) {
                if (!stk.contains(d))
                    stk.push(d);

                if (dependencies.containsKey(d)) {
                    for (String str : dependencies.get(d)) {
                        if (!stk.contains(str))
                            stk.push(str);
                    }
                }
            }
        }
        return stk;
    }

    private static ArrayList<String> tokenize(String str) {
        String delims = "[ (,)]";
        String[] usr = str.split(delims);
        //System.out.println(Arrays.toString(usr));
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < usr.length; i++) {
            if (!usr[i].equals("")) {
                tokens.add(usr[i]);
            }
        }
        return tokens;

    }

}
