# origin_java

origin_java is a Java port of the Origin language — a Maven project that provides `Lexer.java`, `Parser.java`, `ASTNode.java`, `Token.java`, `TokenType.java`, `Nodes.java`, `Interpreter.java`, and `Runner.java`. The project targets Java 17 and depends only on JUnit 5 for tests.

## Build / Test / Lint Commands

- Install: Java 17 (JDK) and Maven 3.8+
- Build: `mvn -q compile`
- Test: `mvn -q test`
- Lint: not configured
- Dev / run: `mvn -q exec:java -Dexec.mainClass=im.manus.Runner` (or run `Runner.java` from your IDE)
- Package: `mvn -q package` (produces `origin-1.7.5.jar`)

## Code Style Rules

- Language/version: Java 17 (per `maven.compiler.source` / `maven.compiler.target` in `pom.xml`)
- Paradigm: classic interpreter pipeline (Lexer → Parser → AST → Interpreter); `ASTNode` is the base for all AST node classes
- Types: explicit, Java 17 features allowed
- Formatting: 4-space indentation, standard Java conventions (no Spotless/Checkstyle config in the repo)
- Imports / module style: top-level package `im.manus`; one public class per file matching the file name
- Dependencies: JUnit 5 (`org.junit.jupiter:junit-jupiter-api:5.10.0`, test scope)

## Verification Criteria

Before claiming any task done, Claude MUST:
1. Run `mvn -q compile` and confirm BUILD SUCCESS with no compile errors.
2. Run `mvn -q test` and confirm the test suite passes.
3. If `Runner` is invoked interactively, run `mvn -q exec:java -Dexec.mainClass=im.manus.Runner` and confirm a clean startup.
4. Report the exact commands run and their outcomes in the final message.

## GitHub account rule (AGENCY-ACCOUNT-RULE)
This folder is a PERSONAL project of boblio-max. For ANY GitHub operation
(gh commands, git push/pull, releases), the active account MUST be
`boblio-max` — NEVER the Storefront Web agency account.
Check first: `gh auth status`. If another account is active, run
`gh auth switch --user boblio-max` before proceeding.
