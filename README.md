Android-Utils
========

Combination of common Android Utils can be used in almost android projects

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
def latest_release_version = "2.0.3"
compile "com.github.duyp.android-utils:core:${latest_release_version}" // core (basic | common utils)
compile "com.github.duyp.android-utils:image:${latest_release_version}" // Image utils (glide, bitmap...)
compile "com.github.duyp.android-utils:navigation:${latest_release_version}" // Navigation utils
compile "com.github.duyp.android-utils:network:${latest_release_version}" // Network utils (SSL / TLS and x509TrustManager)
compile "com.github.duyp.android-utils:adapter:${latest_release_version}" // RecyclerView Adapters
compile "com.github.duyp.android-utils:animation:${latest_release_version}" // Animation utils (YoYo...)
compile "com.github.duyp.android-utils:rx:${latest_release_version}" // Rx utils (custom functions, task helper...)
compile "com.github.duyp.android-utils:view:${latest_release_version}" // View utils, custom views...
compile "com.github.duyp.android-utils:realm:${latest_release_version}" // Realm utils (realm data access objects, realm Live data mapper, Realm live data adapter
```

* Using SNAPSHOT version (see [JitPack][2] for documentation):
```groovy
compile 'com.github.duyp:android-utils:master-SNAPSHOT'
```

Other options: [https://jitpack.io/v/duyp/android-utils.svg][3]

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

