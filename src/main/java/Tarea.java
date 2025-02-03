import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;

public class Tarea {
    private int id;
    private String name;
    private String description;
    private Estado status;
    private Date date;

    public Tarea(int id, String name, String description, Estado status, Date date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Estado getStatus() {
        return status;
    }

    public void setStatus(Estado status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return "ID: " + id + " | Nombre: " + name + " |  " + description + " | Fecha: " + sdf.format(date) + " | [" + status + "]";
    }
}
