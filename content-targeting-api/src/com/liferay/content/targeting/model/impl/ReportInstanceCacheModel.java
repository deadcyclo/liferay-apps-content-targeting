/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.content.targeting.model.impl;

import com.liferay.content.targeting.model.ReportInstance;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing ReportInstance in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @see ReportInstance
 * @generated
 */
public class ReportInstanceCacheModel implements CacheModel<ReportInstance>,
	Externalizable {
	@Override
	public String toString() {
		StringBundler sb = new StringBundler(21);

		sb.append("{reportInstanceId=");
		sb.append(reportInstanceId);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", userName=");
		sb.append(userName);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", reportKey=");
		sb.append(reportKey);
		sb.append(", className=");
		sb.append(className);
		sb.append(", classPK=");
		sb.append(classPK);
		sb.append(", typeSettings=");
		sb.append(typeSettings);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public ReportInstance toEntityModel() {
		ReportInstanceImpl reportInstanceImpl = new ReportInstanceImpl();

		reportInstanceImpl.setReportInstanceId(reportInstanceId);
		reportInstanceImpl.setGroupId(groupId);
		reportInstanceImpl.setCompanyId(companyId);
		reportInstanceImpl.setUserId(userId);

		if (userName == null) {
			reportInstanceImpl.setUserName(StringPool.BLANK);
		}
		else {
			reportInstanceImpl.setUserName(userName);
		}

		if (modifiedDate == Long.MIN_VALUE) {
			reportInstanceImpl.setModifiedDate(null);
		}
		else {
			reportInstanceImpl.setModifiedDate(new Date(modifiedDate));
		}

		if (reportKey == null) {
			reportInstanceImpl.setReportKey(StringPool.BLANK);
		}
		else {
			reportInstanceImpl.setReportKey(reportKey);
		}

		if (className == null) {
			reportInstanceImpl.setClassName(StringPool.BLANK);
		}
		else {
			reportInstanceImpl.setClassName(className);
		}

		reportInstanceImpl.setClassPK(classPK);

		if (typeSettings == null) {
			reportInstanceImpl.setTypeSettings(StringPool.BLANK);
		}
		else {
			reportInstanceImpl.setTypeSettings(typeSettings);
		}

		reportInstanceImpl.resetOriginalValues();

		return reportInstanceImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		reportInstanceId = objectInput.readLong();
		groupId = objectInput.readLong();
		companyId = objectInput.readLong();
		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		modifiedDate = objectInput.readLong();
		reportKey = objectInput.readUTF();
		className = objectInput.readUTF();
		classPK = objectInput.readLong();
		typeSettings = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput)
		throws IOException {
		objectOutput.writeLong(reportInstanceId);
		objectOutput.writeLong(groupId);
		objectOutput.writeLong(companyId);
		objectOutput.writeLong(userId);

		if (userName == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(userName);
		}

		objectOutput.writeLong(modifiedDate);

		if (reportKey == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(reportKey);
		}

		if (className == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(className);
		}

		objectOutput.writeLong(classPK);

		if (typeSettings == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(typeSettings);
		}
	}

	public long reportInstanceId;
	public long groupId;
	public long companyId;
	public long userId;
	public String userName;
	public long modifiedDate;
	public String reportKey;
	public String className;
	public long classPK;
	public String typeSettings;
}