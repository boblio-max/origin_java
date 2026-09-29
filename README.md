# origin_java

this is a Java 17 port of the Origin programming language interpreter. the Python implementation is the main one (`origin` repo) but I wanted to prove the language design wasn't glued to Python, so I rebuilt the whole pipeline in Java: Lexer → Parser → AST → Interpreter.

## how it actually works

classic interpreter pipeline, Maven project (`pom.xml`, currently v1.7.5 — yeah it lags behind Python's v1.7.28, I'll sync it):
- `origin/lexer/` — tokenizer
- `origin/parser/` — recursive descent parser
- `origin/nodes/` — AST node types
- `origin/Runner.java` — entry point, wires it all together

JUnit 5 for tests. see `CLAUDE.md` for build/test commands.

## run it

```bash
mvn compile exec:java
```

## stack

Java 17, Maven, JUnit 5, zero runtime deps. the `im/`, `out/`, `target/` dirs are just build output, ignore them.
