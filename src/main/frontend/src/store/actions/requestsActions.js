import { createAction } from 'redux-actions';

export const CREATE_LEAVE_REQUEST = 'CREATE_LEAVE_REQUEST';
export const CREATE_LEAVE_REQUEST_SUCCESSFUL =
  'CREATE_LEAVE_REQUEST_SUCCESSFUL';
export const CREATE_LEAVE_REQUEST_ERROR = 'CREATE_LEAVE_REQUEST_ERROR';

export const createLeaveRequest = createAction(CREATE_LEAVE_REQUEST);
export const createLeaveRequestSuccessful = createAction(
  CREATE_LEAVE_REQUEST_SUCCESSFUL
);
export const createLeaveRequestError = createAction(CREATE_LEAVE_REQUEST_ERROR);

export const CREATE_SICKNESS_REQUEST = 'CREATE_SICKNESS_REQUEST';
export const CREATE_SICKNESS_REQUEST_SUCCESSFUL =
  'CREATE_SICKNESS_REQUEST_SUCCESSFUL';
export const CREATE_SICKNESS_REQUEST_ERROR = 'CREATE_SICKNESS_REQUEST_ERROR';

export const createSicknessRequest = createAction(CREATE_SICKNESS_REQUEST);
export const createSicknessRequestSuccessful = createAction(
  CREATE_SICKNESS_REQUEST_SUCCESSFUL
);
export const createSicknessRequestError = createAction(
  CREATE_SICKNESS_REQUEST_ERROR
);

export const CREATE_BONUS_REQUEST = 'CREATE_BONUS_REQUEST';
export const CREATE_BONUS_REQUEST_SUCCESSFUL =
  'CREATE_BONUS_REQUEST_SUCCESSFUL';
export const CREATE_BONUS_REQUEST_ERROR = 'CREATE_BONUS_REQUEST_ERROR';

export const createBonusRequest = createAction(CREATE_BONUS_REQUEST);
export const createBonusRequestSuccessful = createAction(
  CREATE_BONUS_REQUEST_SUCCESSFUL
);
export const createBonusRequestError = createAction(CREATE_BONUS_REQUEST_ERROR);
