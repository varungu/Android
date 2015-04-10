package varungu.simpletodolist;

import java.util.Date;

/**
 * Created by Varun on 4/10/2015.
 */
public class Item{
    public int id;
    public String value;
    public Date dueDate;

    public Item(int id, String value)
    {
        this.id = id;
        this.value = value;
        this.dueDate = new Date();
    }
}
