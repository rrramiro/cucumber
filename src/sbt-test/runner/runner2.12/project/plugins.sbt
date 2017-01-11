resolvers += Resolver.file("Local Ivy Repository", file(Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns)

//{
//  val pluginVersion = System.getProperty("plugin.version")
//  if(pluginVersion == null)
//    throw new RuntimeException("""|The system property 'plugin.version' is not defined.
//                                  |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
//  else addSbtPlugin("com.waioeka.sbt" % "cucumber-plugin" % pluginVersion)
//
//}

addSbtPlugin("com.waioeka.sbt" % "cucumber-plugin" % "0.0.9")