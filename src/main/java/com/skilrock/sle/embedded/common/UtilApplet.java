package com.skilrock.sle.embedded.common;

import java.util.List;
import java.util.Map;

import com.skilrock.sle.common.Util;

public class UtilApplet {

	public static int getAdvMessages(Map<String, List<String>> advMsgMap, StringBuilder topMsgsStr, StringBuilder bottomMsgsStr, int appletHeight) {
		String advtMsgEnable = Util.getPropertyValue("ADV_MSG");
		if (advtMsgEnable != null && !"NULL".equalsIgnoreCase(advtMsgEnable) && "YES".equalsIgnoreCase(advtMsgEnable.trim())) {
			if (advMsgMap != null) {
				List<String> topMsgsList = advMsgMap.get("TOP");
				List<String> bottomMsgsList = advMsgMap.get("BOTTOM");
				int msgLen = 0;

				if (topMsgsList != null) {
					if (topMsgsStr.length() == 1) {
						topMsgsStr.deleteCharAt(topMsgsStr.length() - 1);
					}
					for (int i = 0; i < topMsgsList.size(); i++) {
						topMsgsStr = topMsgsStr.append(topMsgsList.get(i) + "~");
						msgLen = topMsgsList.get(i).length();
						if (msgLen > 17) {
							appletHeight = appletHeight + 11 * (msgLen / 17) + 11;
						} else {
							appletHeight = appletHeight + 22;
						}
					}
					if (topMsgsStr.length() > 0) {
						topMsgsStr.deleteCharAt(topMsgsStr.length() - 1);
					}
				}

				if (bottomMsgsList != null) {
					if (bottomMsgsStr.length() == 1) {
						bottomMsgsStr.deleteCharAt(bottomMsgsStr.length() - 1);
					}
					for (int i = 0; i < bottomMsgsList.size(); i++) {
						bottomMsgsStr = bottomMsgsStr.append(bottomMsgsList
								.get(i)
								+ "~");
						msgLen = bottomMsgsList.get(i).length();
						if (msgLen > 17) {
							appletHeight = appletHeight + 11 * (msgLen / 17)
									+ 11;
						} else {
							appletHeight = appletHeight + 22;
						}
					}
					if (bottomMsgsStr.length() > 0) {
						bottomMsgsStr.deleteCharAt(bottomMsgsStr.length() - 1);
					}
				}
			}
		}

		return appletHeight;
	}
}