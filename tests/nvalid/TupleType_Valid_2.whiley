import println from whiley.lang.System

function f(int x) => (int, int):
    return (x, x + 2)

method main(System.Console sys) => void:
    x = f(1)
    sys.out.println(Any.toString(x))