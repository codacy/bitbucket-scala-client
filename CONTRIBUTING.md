# Contributing

One of the great things about open source projects is that anyone can contribute code.
bitbucket-scala-client is a small project and we want to encourage everyone to submit their
patches freely. To help you in that process, there are several things that you should keep in mind.

## Use Pull Requests

If you want to submit code, please use a GitHub pull request.
This is the fastest way for us to evaluate your code and to merge it into the code base.
Please don't file an issue with snippets of code. Doing so means that we need to manually
merge the changes in and update any appropriate tests. That decreases the likelihood that
your code is going to get included in a timely manner. Please use pull requests.

## Unit Tests

Regardless of the changes you are making, please make sure that your pull request includes
appropriate unit tests.

## Testing

Along with submitting unit tests, please make sure that you have used the built-in
testing capabilities in the bitbucket-scala-client build system. At a minimum, you should
run `activator test`. That generates all of the built files and runs all of the unit tests
in the project.
