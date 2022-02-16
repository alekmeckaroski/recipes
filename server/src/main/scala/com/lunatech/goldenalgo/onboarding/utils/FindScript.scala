package com.lunatech.goldenalgo.onboarding.utils

import org.mdedetrich.SbtDigestReverseRouter.findVersionedAsset

object FindScript {

  lazy val client: String = FindScript(
    Asset("client-opt/", "main.js"),
    Asset("client-fastopt/", "main.js")
  )

  lazy val clientDep: String = FindScript(
    Asset("", "client-jsdeps.min.js"),
    Asset("", "client-jsdeps.js")
  )

  private case class Asset(path: String, name: String) {
    val filePath = s"$path$name"
  }

  private def apply(assets: Asset*): String =
    assets
      .find(asset => getClass.getResource(s"/public/${asset.filePath}") != null)
      .map(asset => s"assets/${findVersionedAsset(asset.path, asset.name)}")
      .getOrElse("Script not found")
}
