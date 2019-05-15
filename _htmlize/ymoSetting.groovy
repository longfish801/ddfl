/*
 * ymoSetting.groovy
 *
 * Copyright (C) io.github.longfish801 All Rights Reserved.
 */
import org.apache.commons.io.FilenameUtils;

yakumo.setting {
	// 拡張子tpacのファイルをクロージャマップとして読みこみます
	fileFinder.find(convDir.name, ['*.tpac'], []).each { engine.clmapServer.soak(it.value) }
	engine.bindClmap(convDir.name, ['engine': engine]);
	// templateフォルダ内にある拡張子htmlのファイルをすべてテンプレートとして読みこみます
	fileFinder.find("${convDir.name}/template", ['*.html'], []).each {
		engine.templateHandler.load(FilenameUtils.getBaseName(it.key), it.value)
	}
	// assetフォルダ配下を固定ファイルとして読みこみます
	assetHandler.remove('_html');	// HTML変換の固定ファイルはコピー対象外とします
	assetHandler.gulp(convDir.name, fileFinder.find("${convDir.name}/asset", [], []));
}
