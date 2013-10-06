# Spring Liquibase extensions: examples

## (Temporary) setup

 1. Clone [this forked version](https://github.com/fbiville/liquibase) of Liquibase
 1. `cd path/to/liquibase/fork; git checkout 3.0.x; mvn clean install -DskipTests`
 1. Clone [spring-liquibase-extensions](https://github.com/LateralThoughts/spring-liquibase-extensions)
 1. `cd path/to/spring-liquibase-extensions; mvn clean install`
 1. Clone [me](https://github.com/LateralThoughts/spring-liquibase-extensions-examples)

And now read `Scenario steps` if you're in a hurry.

## SpringLiquibaseChecker

### Application

The application uses Liquibase to maintain its beer database.
Liquibase changelog file is located under `src/main/resources/META-INF/master.xml`.

As configured in the application POM file, `liquibase-maven-plugin` executes the
migrations.

On the other hand, unrun migrations are to be avoided, as it might imply the
application codebase is ahead of the database state.

To achieve this, `SpringLiquibaseChecker` is set up in the application Spring
context file (see `Application.java`). This bean aims at detecting "dirty"
changesets, i.e., changeset that need to be run (excluding changesets configured
to always execute).

### Scenario steps

 * `cd path/to/spring-liquibase-extensions-examples`
 * `mvn clean package` #`clean` in case you built it before, else drop it
 * `java -jar target/spring-liquibase-checker.jar`

And BOOM! An error message similar to the following one should be displayed:

<pre>
    ... com.github.lateralthoughts.liquibase.UnexpectedLiquibaseChangesetException:
    --
    2 changeset(s) has/have to run.
        create_beers_table (META-INF/migrations/init.xml)
        add_beers (META-INF/migrations/init.xml)
    This does *NOT* include changesets marked as 'alwaysRun'
    --
</pre>

Indeed, dude! Those migrations have not been run yet!

_Please note there is a 3rd migration in this app, but `SpringLiquibaseChecker` wisely ignores it
given this migration is configured to always run._

Then, let's fix it and finally start the app:

 * `mvn liquibase:updateSQL` #check the migration file, as you *always* do ;-)
 * `mvn liquibase:update` #actually run the migrations
 * `java -jar target/spring-liquibase-checker.jar`

And that's it!
Now you view your beautiful app by browsing to: `http://localhost:8080/beers`

## Powered By <3

Thanks to [Nathan Voxland](https://twitter.com/nvoxland) for his advice on `SpringLiquibaseChecker`.

This demo app is proudly powered with:

 * [love](http://www.lateral-thoughts.com/)
 * [Spring Boot](https://github.com/spring-projects/spring-boot)
 * Spring JDBC