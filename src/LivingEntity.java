public abstract class LivingEntity {
    public final Coord pos;
    public final Stats stats;
    private boolean facingLeft;
    private boolean moving;
    private boolean attacking;

    public LivingEntity(Coord pos, Stats stats) {
        this.pos = pos;
        this.stats = stats;
        facingLeft = false;
        moving = attacking = false;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public boolean isMoving() {
        return moving;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void setMoving(boolean b) {
        moving = b;
    }

    public void setAttacking(boolean b) {
        attacking = b;
    }

    public void face(Coord direction) {
        if (direction.x != 0) {
            facingLeft = direction.x < 0;
        }
    }

    public void move(Coord direction) {
        pos.add(direction);
    }

    public void takeDamage(int damage) {
        stats.health -= damage;
    }
}
