public class Clue {
    public int count;
    public int color;
    public boolean solved = false;

    public Clue(int count, int color) {
        this.count = 0; 
        this.color = 0;
    }

    public void setColor(int color) {
        this.color = color;
    }


    public void setCount(int count) {
        this.count = count;
    }
}