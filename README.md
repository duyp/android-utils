Android-Utils
========

Combination of common Android Utils used in almost adroid projects

Download
--------

Gradle:

Add the JitPack repository to your project level 's gradle file

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
  
Add the dependency to modul 's gradle file

* Using [latest release][1]:
```groovy
compile 'com.github.duyp:android-utils:latest-release-version'
```

* Using SNAPSHOT version (see [JitPack][2] for documentation):
```groovy
compile 'com.github.duyp:android-utils:master-SNAPSHOT'
```

Other options: (https://jitpack.io/v/duyp/android-utils.svg][3]

License
=======

    Copyright 2017 Duy Pham.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[1]: https://github.com/duyp/android-utils/releases
[2]: https://jitpack.io/docs/#snapshots
[3]: https://jitpack.io/#duyp/android-utils

