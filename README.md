# CRaSH integration for Roller blog

## Installation

* Roller jar is required for compiling this project. Unfortunately Roller jars are not in central so it requires
to compile Roller locally which is quite trivial
* Build this project
* Deploy roller in your Servlet Container
* Add the following jars in roller _WEB-INF/lib_
    * _crash.shell-1.3.0-beta11-standalone.jar_ (http://search.maven.org/remotecontent?filepath=org/crashub/crash.shell/1.3.0-beta11/crash.shell-1.3.0-beta11-standalone.jar)
    * _crash.connectors.ssh-1.3.0-beta11-standalone.jar_ (http://search.maven.org/remotecontent?filepath=org/crashub/crash.connectors.ssh/1.3.0-beta11/crash.connectors.ssh-1.3.0-beta11-standalone.jar)
    * _rollercrash-1.0-SNAPSHOT.jar_ (build by this project)
* Copy the dir _src/crash_ in roller _WEB-INF_
* Modify _WEB-INF/web.xml_ to add CRaSH listener


    &lt;listener&gt;
      &lt;listener-class&gt;org.crsh.plugin.WebPluginLifeCycle&lt;/listener-class&gt;
    &lt;/listener&gt;


## Usage

SSH with a Roller username/password

    ssh -p 2000 user@localhost

