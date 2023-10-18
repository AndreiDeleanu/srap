package com.ibm.srap.services;

import com.ibm.srap.client_beans.NotificationDO;
import com.ibm.srap.client_beans.OperationResult;

public interface NotificationService {
	
	OperationResult sendNotification(NotificationDO notification);

}
