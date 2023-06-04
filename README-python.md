# Installation
You need to have access to Python. For simplicity, we do not use any other python libraries. Sometimes, using the standard python version is not the version you want. We use Python 3.x, in general we try to keep up with the latest versions. But due to the nature of the code, most 3.x versions should work.

## Use VirtualEnv to work with multiple Python versions
You can find documentation for [installation here](https://virtualenv.pypa.io/en/latest/installation.html). The preferred way to install virtualenv is through pipx. The following code block shows how to do this on the Mac, refer to the link for other platforms.

```bash
# Install virtualenv
brew install pipx
pipx install virtualenv

# Create the environments
virtualenv -p python3.9 .venv3-9
virtualenv -p python3.11 .venv3-11

# Activate an environment and deactivate it again.
source .venv3-9/bin/activate
deactivate
```

# Running the tests
Running unit tests is easy when working in a tool like Jetbrains PyCharm. You can click the run button next to a single test or to a complete class.

You can also do it from the command line with an active Python environment. Run all the tests in one specific file
```bash
python -m unittest tests/test_purchase_order.py
```
And if you want to run all the tests in a specific folder
```
python -m unittest discover tests/
```
