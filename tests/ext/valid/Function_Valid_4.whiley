import * from whiley.lang.*

define fr4nat as int where $ >= 0

fr4nat g(fr4nat x):
    return x + 1

string f(fr4nat x):
    return Any.toString(x)

void ::main(System.Console sys,[string] args):
    y = 1
    sys.out.println(f(g(y)))
