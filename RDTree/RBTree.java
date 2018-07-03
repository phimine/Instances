public class RBTree<T extends Comparable<T>> {
    /**
     * 根节点
     */
    private RBNode<T> root;

    public RBTree() {
        this.root = null;
    }

    public RBNode<T> getRoot() {
        return root;
    }

    /**
     * 前序遍历打印树
     */
    public void preOrder() {
        this.preOrder(this.root);
    }

    /**
     * 中序遍历打印树
     */
    public void inOrder() {
        this.inOrder(this.root);
    }

    /**
     * 后序遍历打印树
     */
    public void postOrder() {
        this.postOrder(this.root);
    }

    /**
     * 递归查找指键值为key的节点
     *
     * @param key
     * @return
     */
    public RBNode<T> search(T key) {
        return this.search(this.root, key);
    }

    /**
     * 非递归查找键值为key的节点
     *
     * @param key
     * @return
     */
    public RBNode<T> whileSearch(T key) {
        RBNode<T> tree = this.root;
        while (tree != null) {
            int compared = key.compareTo(tree.getKey());
            if (compared < 0) {
                tree = tree.getLeft();
            } else if (compared > 0) {
                tree = tree.getRight();
            } else {
                break;
            }
        }
        return tree;
    }

    /**
     * 查找最小节点
     *
     * @return
     */
    public RBNode<T> minimum() {
        return this.minimum(this.root);
    }

    /**
     * 查找最大节点
     *
     * @return
     */
    public RBNode<T> maximum() {
        return this.maximum(this.root);
    }

    /**
     * 查找后继节点，两种可能：
     * 1. 右子节点中最小的节点
     * 2. 某个祖先节点（属于最近的祖先节点的左分支）
     *
     * @param node
     * @return
     */
    public RBNode<T> successor(RBNode<T> node) {
        if (node == null) {
            return null;
        }
        if (node.getRight() != null) {
            return this.minimum(node.getRight());
        }
        RBNode<T> parent = node.getParent();
        while (parent != null && node != parent.getLeft()) {
            node = parent;
            parent = parent.getParent();
        }
        return parent;
    }

    /**
     * 查找前驱节点，两种可能：
     * 1. 左子节点中最大的节点
     * 2. 某个祖先节点（属于最近的祖先节点的右分支）
     *
     * @param node
     * @return
     */
    public RBNode<T> predecessor(RBNode<T> node) {
        if (node == null) {
            return null;
        }
        if (node.getLeft() != null) {
            return this.maximum(node.getLeft());
        }
        RBNode<T> parent = node.getParent();
        while (parent != null && node != parent.getRight()) {
            node = parent;
            parent = parent.getParent();
        }
        return parent;
    }

    /**
     * 插入节点
     * @param node
     */
    public void insert(RBNode<T> node) {
        // 1. 插入新节点
        RBNode<T> x = this.root;
        RBNode<T> xp = null;
        while (x != null){
            int compared = node.getKey().compareTo(x.getKey());
            xp = x;
            if (compared < 0){
                x = x.getLeft();
            } else {
                x = x.getRight();
            }
        }
        node.setParent(xp);
        if (xp == null){
            this.root = node;
        } else {
            int compared = node.getKey().compareTo(xp.getKey());
            if (compared < 0){
                xp.setLeft(node);
            } else {
                xp.setRight(node);
            }
        }

        // 2. 节点颜色设为红
        node.setRed();

        // 3. 修正红黑树
        this.insertFixUp(node);
    }

    /**
     * 插入操作
     * @param key
     */
    public void insert(T key){
        if (null != key) {
            this.insert(new RBNode<>(key, true, null, null, null));
        }
    }

    /**
     * 删除操作
     * @param key
     */
    public void remove(T key) {
        RBNode<T> node = this.search(key);
        if (null != node){
            this.remove(node);
        }
    }

    /**
     * 释放
     */
    public void clear() {
        this.destory(this.root);
    }

    public void print() {
        this.print(this.root, 0);
    }

    private void print(RBNode<T> tree, int position) {
        if (null == tree) {
            return;
        }
        String info = tree.getKey() + "(" + (tree.isRed() ? "RED" : "BLACK") + ")";
        if (position == 0) {
            System.out.println("root: " + info);
        } else {
            String pos = position > 0 ? "right" : "left";
            System.out.println(info + ": " + pos + " child of " + tree.getParent().getKey());
        }
        print(tree.getLeft(), -1);
        print(tree.getRight(), 1);
    }

    private void preOrder(RBNode<T> tree) {
        if (null != tree) {
            System.out.print(tree.getKey() + " ");
            preOrder(tree.getLeft());
            preOrder(tree.getRight());
        }
    }

    private void inOrder(RBNode<T> tree) {
        if (null != tree) {
            preOrder(tree.getLeft());
            System.out.print(tree.getKey() + " ");
            preOrder(tree.getRight());
        }
    }

    private void postOrder(RBNode<T> tree) {
        if (null != tree) {
            preOrder(tree.getLeft());
            preOrder(tree.getRight());
            System.out.print(tree.getKey() + " ");
        }
    }

    private RBNode<T> search(RBNode<T> tree, T key) {
        if (tree == null) {
            return null;
        }
        int compared = key.compareTo(tree.getKey());
        if (compared < 0) {
            return this.search(tree.getLeft(), key);
        } else if (compared > 0) {
            return this.search(tree.getRight(), key);
        } else {
            return tree;
        }
    }

    private void remove(RBNode<T> node) {
        RBNode<T> child, parent;
        boolean color;

        // 被删除节点的"左右孩子都不为空"的情况。
        if ((node.getLeft() != null) && (node.getRight() != null)) {
            // 被删节点的后继节点。(称为"取代节点")
            // 用它来取代"被删节点"的位置，然后再将"被删节点"去掉。
            RBNode<T> replace = node;

            // 获取后继节点
            replace = replace.getRight();
            while (replace.getLeft() != null)
                replace = replace.getLeft();

            // "node节点"不是根节点(只有根节点不存在父节点)
            RBNode<T> np = node.getParent();
            if (np != null) {
                if (np.getLeft() == node)
                    np.setLeft(replace);
                else
                    np.setRight(replace);
            } else {
                // "node节点"是根节点，更新根节点。
                this.root = replace;
            }

            // child是"取代节点"的右孩子，也是需要"调整的节点"。
            // "取代节点"肯定不存在左孩子！因为它是一个后继节点。
            child = replace.getRight();
            parent = replace.getParent();
            // 保存"取代节点"的颜色
            color = replace.isRed();

            // "被删除节点"是"它的后继节点的父节点"
            if (parent == node) {
                parent = replace;
            } else {
                // child不为空
                if (child != null)
                    child.setParent(parent);
                parent.setLeft(child);

                replace.setRight(node.getRight());
                node.getRight().setParent(replace);
            }

            replace.setParent(np);
            replace.setRed(node.isRed());
            replace.setLeft(node.getLeft());
            node.getLeft().setParent(replace);

            if (!color)
                removeFixUp(child, parent);

            node = null;
            return;
        }

        if (node.getLeft() != null) {
            child = node.getLeft();
        } else {
            child = node.getRight();
        }

        parent = node.getParent();
        // 保存"取代节点"的颜色
        color = node.isRed();

        if (child != null)
            child.setParent(parent);

        // "node节点"不是根节点
        if (parent != null) {
            if (parent.getLeft() == node)
                parent.setLeft(child);
            else
                parent.setRight(child);
        } else {
            this.root = child;
        }

        if (!color)
            removeFixUp(child, parent);
        node = null;
    }

    /**
     * 删除修复，红黑树在删除节点后可能会破坏红黑规则失去平衡，需要调用该函数以修复
     *
     * @param node 替代节点
     * @param parent 替代节点的父节点
     */
    private void removeFixUp(RBNode<T> node, RBNode<T> parent) {
        RBNode<T> other;

        while ((node == null || !node.isRed()) && (node != this.root)) {
            if (parent.getLeft() == node) {
                other = parent.getRight();
                if (other.isRed()) {
                    // Case 1: x的兄弟w是红色的
                    other.setBlack();
                    parent.setRed();
                    leftRotate(parent);
                    other = parent.getRight();
                }

                if ((other.getLeft() == null || !other.getLeft().isRed()) &&
                        (other.getRight() == null || !other.getRight().isRed())) {
                    // Case 2: x的兄弟w是黑色，且w的俩个孩子也都是黑色的
                    other.setRed();
                    node = parent;
                    parent = node.getParent();
                } else {

                    if (other.getRight() == null || !(other.getRight().isRed())) {
                        // Case 3: x的兄弟w是黑色的，并且w的左孩子是红色，右孩子为黑色。
                        other.getLeft().setBlack();
                        other.setRed();
                        rightRotate(other);
                        other = parent.getRight();
                    }
                    // Case 4: x的兄弟w是黑色的；并且w的右孩子是红色的，左孩子任意颜色。
                    other.setRed(parent.isRed());
                    parent.setBlack();
                    other.getRight().setBlack();
                    leftRotate(parent);
                    node = this.root;
                    break;
                }
            } else {

                other = parent.getLeft();
                if (other.isRed()) {
                    // Case 1: x的兄弟w是红色的
                    other.setBlack();
                    parent.setRed();
                    rightRotate(parent);
                    other = parent.getLeft();
                }

                if ((other.getLeft() == null || !(other.getLeft().isRed())) &&
                        (other.getRight() == null || !(other.getRight().isRed()))) {
                    // Case 2: x的兄弟w是黑色，且w的俩个孩子也都是黑色的
                    other.setRed();
                    node = parent;
                    parent = node.getParent();
                } else {

                    if (other.getLeft() == null || !(other.getLeft().isRed())) {
                        // Case 3: x的兄弟w是黑色的，并且w的左孩子是红色，右孩子为黑色。
                        other.getRight().setBlack();
                        other.setRed();
                        leftRotate(other);
                        other = parent.getLeft();
                    }

                    // Case 4: x的兄弟w是黑色的；并且w的右孩子是红色的，左孩子任意颜色。
                    other.setRed(parent.isRed());
                    parent.setBlack();
                    other.getLeft().setBlack();
                    rightRotate(parent);
                    node = this.root;
                    break;
                }
            }
        }

        if (node != null) {
            node.setBlack();
        }
    }

    /**
     * 插入修复，红黑树在插入节点后可能会破坏红黑规则失去平衡，插入后需要调用该函数以修复
     *
     * @param node 插入的节点
     */
    private void insertFixUp(RBNode<T> node) {
        RBNode<T> p = null;
        RBNode<T> gp = null;
        // 判断父节点的颜色，如果父节点为空或者为黑色，无需处理
        while ((p = node.getParent()) != null && p.isRed() && (gp = p.getParent()) != null) {
            // 父节点是祖父节点的左子节点
            if (p == gp.getLeft()){
                /** case#1：叔叔节点是红色，不平衡的红色因素往上移
                 *
                 *         丨                           丨
                 *        gp(B)                        gp(R)(current)
                 *       /   \                        /   \
                 *     p(R) uncle(R)     -->      p(B)  uncle(B)
                 *    /                            /
                 * node(R)(current)             node(R)
                 */
                RBNode uncle = gp.getRight();
                if (uncle != null && uncle.isRed()){
                    gp.setRed();
                    p.setBlack();
                    uncle.setBlack();
                    node = gp;
                } else {
                    /**
                     * case#2：叔叔节点是黑色，并且节点位于父节点的右子节点，操作变成case#3
                     *
                     *         丨                           丨                           丨
                     *        gp(B)                        gp(B)                        gp(B)
                     *       /   \                        /   \                        /   \
                     *     p(R) uncle(B)     -->      node(R) uncle(B)     -->      p(R)  uncle(B)
                     *       \                        /                             /
                     *      node(R)                p(R)                           node(R)
                     */
                    if (node == p.getRight()){
                        this.leftRotate(p);
                        RBNode<T> temp;
                        temp = p;
                        p = node;
                        node = p;
                    }

                    /**
                     * case#3：叔叔节点是黑色，并且节点位于父节点的左子节点，以父节点为支点右旋
                     *
                     *         丨                           丨                           丨
                     *        gp(B)                        gp(R)                        P(B)
                     *       /   \                        /   \                        /   \
                     *     p(R) uncle(B)     -->      p(B) uncle(B)     -->      node(R)  gp(R)
                     *     /                          /                                      \
                     *   node(R)                    node(R)                                 uncle(B)
                     */
                    gp.setRed();
                    p.setBlack();
                    this.rightRotate(gp);
                }
            } else { // 父节点是祖父节点的右子节点，与左子节点情况操作相反
                RBNode<T> uncle = gp.getLeft();
                /** case#1：叔叔节点是红色，不平衡的红色因素往上移
                 *
                 *         丨                            丨
                 *        gp(B)                        gp(R)(current)
                 *       /   \                        /   \
                 *     p(R) uncle(R)     -->      p(B)  uncle(B)
                 *    /                            /
                 * node(R)(current)             node(R)
                 */
                if (uncle != null && uncle.isRed()){
                    gp.setRed();
                    uncle.setBlack();
                    p.setBlack();
                    node = gp;
                } else {
                    /**
                     * case#2：叔叔节点为黑色，当前节点为左子节点，操作转换成case#3
                     *        丨                         丨                           丨
                     *       gp(B)                       gp(B)                        gp(B)
                     *      /   \                      /   \                         /   \
                     * uncle(B) p(R)      -->     uncle(B) node(R)      -->     uncle(B) p(R)
                     *         /                             \                             \
                     *       node(R)                         p(R)                         node(R)
                     */
                    if (node == p.getLeft()){
                        this.rightRotate(p);
                        RBNode<T> temp;
                        temp = p;
                        p = node;
                        node = temp;
                    } else {
                        /**
                         * case#3：叔叔节点为黑色，当前节点为左右子节点，以父节点为支点左旋
                         *        丨                         丨                        丨
                         *       gp(B)                       gp(R)                     p(B)
                         *      /   \                      /   \                      /   \
                         * uncle(B) p(R)      -->     uncle(B) p(B)      -->       gp(R) node(R)
                         *            \                           \                /
                         *           node(R)                    node(R)        uncle(B)
                         */
                        gp.setRed();
                        p.setBlack();
                        this.leftRotate(gp);
                    }
                }
            }
        }

        // 根节点设置为黑色
        this.root.setBlack();
    }

    private RBNode<T> minimum(RBNode<T> node) {
        if (node == null) {
            return null;
        }
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    private RBNode<T> maximum(RBNode<T> node) {
        if (node == null) {
            return null;
        }
        while (node.getRight() != null) {
            node = node.getRight();
        }

        return node;
    }

    /**
     * 左旋
     *       x                y
     *     / \              / \
     *   xl   y   -->     x   yr
     *       / \        / \
     *     yl   yr    xl   yl
     *
     * @param x
     */
    private void leftRotate(RBNode<T> x) {
        // 1. y的左子节点变成x的右子节点
        RBNode<T> y = x.getRight();
        RBNode<T> yl = y.getLeft();
        y.setLeft(null);
        x.setRight(yl);
        if (null != yl) {
            yl.setParent(x);
        }

        // 2. 处理y和x父节点关系
        RBNode<T> xp = x.getParent();
        y.setParent(xp);
        if (xp == null) {
            this.root = y;
        } else if (xp.getLeft() == x) {
            xp.setLeft(y);
        } else if (xp.getRight() == x) {
            xp.setRight(y);
        }

        // 3. 处理y和x关系
        x.setParent(y);
        y.setLeft(x);
    }

    /**
     * 右旋，示例图：
     *       x                y
     *     / \              / \
     *   y   xr   -->     yl   x
     * / \                   / \
     * yl   yr               yr   xr
     *
     * @param x
     */
    private void rightRotate(RBNode<T> x) {
        // 1. y的右节点变成x的左节点
        RBNode<T> y = x.getLeft();
        RBNode<T> yr = y.getRight();
        y.setRight(null);
        x.setLeft(yr);
        if (yr != null) {
            yr.setParent(x);
        }

        // 2. 处理y和x父节点关系
        RBNode<T> xp = x.getParent();
        y.setParent(xp);
        if (xp == null) {
            this.root = y;
        } else if (xp.getRight() == x) {
            xp.setRight(y);
        } else if (xp.getLeft() == x) {
            xp.setLeft(y);
        }

        // 3. 处理y和x关系
        x.setParent(y);
        y.setRight(x);

    }

    private void destory(RBNode<T> tree) {
        if (null != tree) {
            this.destory(tree.getLeft());
            this.destory(tree.getRight());
            tree = null;
        }
    }

}
