/*
 * Copyright 2015 ligaDATA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ligadata.MigrateBase;

public class DataFormat {
	public String containerName = null;
	public long timePartition = 0;
	public String[] bucketKey = null;
	public long transactionid = 0;
	public int rowid = 0;
	public String serializername = null;
	public String data = null; // In JSON format generated from GSON

	public DataFormat(String tcontainerName, long ttimePartition,
			String[] tbucketKey, long ttransactionid, int trowid,
			String tserializername, String tdata) {
		containerName = tcontainerName;
		timePartition = ttimePartition;
		bucketKey = tbucketKey;
		transactionid = ttransactionid;
		rowid = trowid;
		serializername = tserializername;
		data = tdata;
	}
}
