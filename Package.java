import java.util.ArrayList;

/**
 * Created by berkeyanilmaz on 2017-03-30.
 */
public class Package {
    private String name;
    private ArrayList<String> dependencies;
    private boolean valid;

    public Package(String name, ArrayList<String> dependencies) throws IllegalArgumentException {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Package name invalid");
        }
        if (dependencies == null) {
            throw new IllegalArgumentException("Dependencies cannot be null.");
        }

        this.name = name;
        this.dependencies = dependencies;
        valid = false;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getDependencies() {
        return (ArrayList<String>) dependencies.clone();
    }

    public void setValid(boolean a) {
        this.valid = a;
    }

    public boolean isValid() {
        return this.valid;
    }

    public String toString() {
        return name;
    }
}
