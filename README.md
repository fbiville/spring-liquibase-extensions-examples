# Spring Liquibase extensions: examples

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

#### Blocked!

 * `cd path/to/spring-liquibase-extensions-examples`
 * `git checkout -f almost_fix_budweiser`
 * `mvn clean package` #`clean` in case you built it before, else drop it
 * `java -jar target/spring-liquibase-checker.jar`

And BOOM! An error message similar to the following one should be displayed:

<pre>
    ... com.github.lateralthoughts.liquibase.UnexpectedLiquibaseChangesetException:
    --
    3 changeset(s) has/have to run.
        create_beers_table (META-INF/migrations/init.xml)
        add_beers (META-INF/migrations/init.xml)
        always_and_on_change (META-INF/migrations/alwaysAndOnChange.xml)
    This does *NOT* include changesets marked as 'alwaysRun'...
        ...(unless they are marked as 'runOnChange' and have been altered).
    --
</pre>

Indeed, dude! Those migrations have not been run yet!

_Please note there is another migration in this app (`always.xml`), but `SpringLiquibaseChecker` wisely ignores it
given this migration is configured to always run (and is not supposed to run on change)._

#### Fixed!

Then, let's fix it and finally start the app:

 * `mvn liquibase:updateSQL` #check the migration file, as you *always* do ;-)
 * `mvn liquibase:update` #actually run the migrations
 * `java -jar target/spring-liquibase-checker.jar`

And that's it!
Now you view your beautiful app by browsing to: `http://localhost:8080/beers`

#### New fix!

But damn! Another bug has been noticed.
A brand has been misspelled again: 'BADWEISER' should be 'BUDWEISER'.

However, lucky you: the faulty changeset has already been configured to run on change, you just have to fix it.

To do so:

 * `git checkout -f master`
 
For the fun of it, try starting the app:

 * `mvn package` #not clean!
 * `java -jar target/spring-liquibase-checker.jar`

You should see a familiar stack trace:

<pre>
    ... com.github.lateralthoughts.liquibase.UnexpectedLiquibaseChangesetException:
    --
    1 changeset(s) has/have to run.
        always_and_on_change (META-INF/migrations/alwaysAndOnChange.xml)
    This does *NOT* include changesets marked as 'alwaysRun'...
        ...(unless they are marked as 'runOnChange' and have been altered).
    --
</pre>

Although this is a changeset configured to always run, it must also be executed when its contents are altered.
Given you just changed it, `SpringLiquibaseChecker` will prevent the application from starting.

To finally fix everything:

 * `mvn liquibase:updateSQL` #check the migration file, as you *always* do ;-)
 * `mvn liquibase:update` #actually run the migrations
 * `java -jar target/spring-liquibase-checker.jar`

## Powered By <3

Thanks to [Nathan Voxland](https://twitter.com/nvoxland) for his advice on `SpringLiquibaseChecker`.

This demo app is proudly powered with:

 * [love](http://www.lateral-thoughts.com/)
 * [Spring Boot](https://github.com/spring-projects/spring-boot)
 * Spring JDBC
