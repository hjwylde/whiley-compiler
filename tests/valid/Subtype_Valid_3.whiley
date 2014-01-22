import println from whiley.lang.System

type sr8nat is (int n) where n > 0

type sr8tup is {sr8nat f, int g} where g > f

method main(System.Console sys) => void:
    x = [{f: 1, g: 3}, {f: 4, g: 8}]
    x[0].f = 2
    sys.out.println(Any.toString(x))
