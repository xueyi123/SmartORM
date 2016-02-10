/**
 * Copyright (c) 2011-2016, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iih5.smartorm.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.iih5.smartorm.kit.StringKit;

/**
 * Model 生成器
 */
public class ModelGenerator {
	
	protected String packageTemplate =
		"package %s;%n%n";
	protected String importTemplate =
		"import %s.%s;%n%n";
	protected String classDefineTemplate =
		"/**%n" +
		" * Generated by JFinal.%n" +
		" */%n" +
		"@SuppressWarnings(\"serial\")%n" +
		"public class %s extends %s<%s> {%n";
	protected String daoTemplate =
			"\tpublic static final %s dao = new %s();%n";
	
	protected String modelPackageName;
	protected String baseModelPackageName;
	protected String modelOutputDir;
	protected boolean generateDaoInModel = true;
	
	public ModelGenerator(String modelPackageName, String baseModelPackageName, String modelOutputDir) {
		if (StringKit.isBlank(modelPackageName))
			throw new IllegalArgumentException("modelPackageName can not be blank.");
		if (modelPackageName.contains("/") || modelPackageName.contains("\\"))
			throw new IllegalArgumentException("modelPackageName error : " + modelPackageName);
		if (StringKit.isBlank(baseModelPackageName))
			throw new IllegalArgumentException("baseModelPackageName can not be blank.");
		if (baseModelPackageName.contains("/") || baseModelPackageName.contains("\\"))
			throw new IllegalArgumentException("baseModelPackageName error : " + baseModelPackageName);
		if (StringKit.isBlank(modelOutputDir))
			throw new IllegalArgumentException("modelOutputDir can not be blank.");
		
		this.modelPackageName = modelPackageName;
		this.baseModelPackageName = baseModelPackageName;
		this.modelOutputDir = modelOutputDir;
	}
	
	public void setGenerateDaoInModel(boolean generateDaoInModel) {
		this.generateDaoInModel = generateDaoInModel;
	}
	
	public void generate(List<TableMeta> tableMetas) {
		System.out.println("Generate model ...");
		for (TableMeta tableMeta : tableMetas)
			genModelContent(tableMeta);
		wirtToFile(tableMetas);
	}
	
	protected void genModelContent(TableMeta tableMeta) {
		StringBuilder ret = new StringBuilder();
		genPackage(ret);
		genImport(tableMeta, ret);
		genClassDefine(tableMeta, ret);
		genDao(tableMeta, ret);
		ret.append(String.format("}%n"));
		tableMeta.modelContent = ret.toString();
	}
	
	protected void genPackage(StringBuilder ret) {
		ret.append(String.format(packageTemplate, modelPackageName));
	}
	
	protected void genImport(TableMeta tableMeta, StringBuilder ret) {
		ret.append(String.format(importTemplate, baseModelPackageName, tableMeta.baseModelName));
	}
	
	protected void genClassDefine(TableMeta tableMeta, StringBuilder ret) {
		ret.append(String.format(classDefineTemplate, tableMeta.modelName, tableMeta.baseModelName, tableMeta.modelName));
	}
	
	protected void genDao(TableMeta tableMeta, StringBuilder ret) {
		if (generateDaoInModel)
			ret.append(String.format(daoTemplate, tableMeta.modelName, tableMeta.modelName));
		else
			ret.append(String.format("\t%n"));
	}
	
	protected void wirtToFile(List<TableMeta> tableMetas) {
		try {
			for (TableMeta tableMeta : tableMetas)
				wirtToFile(tableMeta);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 若 model 文件存在，则不生成，以免覆盖用户手写的代码
	 */
	protected void wirtToFile(TableMeta tableMeta) throws IOException {
		File dir = new File(modelOutputDir);
		if (!dir.exists())
			dir.mkdirs();
		
		String target = modelOutputDir + File.separator + tableMeta.modelName + ".java";
		
		File file = new File(target);
		if (file.exists()) {
			return ;	// 若 Model 存在，不覆盖
		}
		
		FileWriter fw = new FileWriter(file);
		try {
			fw.write(tableMeta.modelContent);
		}
		finally {
			fw.close();
		}
	}
}


