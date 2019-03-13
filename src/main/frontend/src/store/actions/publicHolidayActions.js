import { createAction } from 'redux-actions';

export const REQUEST_PUBLIC_HOLIDAY = 'REQUEST_PUBLIC_HOLIDAY';
export const RECEIVE_PUBLIC_HOLIDAY = 'RECEIVE_PUBLIC_HOLIDAY';
export const RECEIVE_PUBLIC_HOLIDAY_ERROR = 'RECEIVE_PUBLIC_HOLIDAY_ERROR';

export const requestPublicHoliday = createAction(REQUEST_PUBLIC_HOLIDAY);
export const receivePublicHoliday = createAction(RECEIVE_PUBLIC_HOLIDAY);
export const errorReceivingPublicHoliday = createAction(
  RECEIVE_PUBLIC_HOLIDAY_ERROR
);

export const REMOVE_PUBLIC_HOLIDAY = 'REMOVE_PUBLIC_HOLIDAY';
export const REMOVE_PUBLIC_HOLIDAY_SUCCESS = 'REMOVE_PUBLIC_HOLIDAY_SUCCESS';
export const REMOVE_PUBLIC_HOLIDAY_ERROR = 'REMOVE_PUBLIC_HOLIDAY_ERROR';

export const removePublicHoliday = createAction(REMOVE_PUBLIC_HOLIDAY);
export const removePublicHolidaySuccess = createAction(
  REMOVE_PUBLIC_HOLIDAY_SUCCESS
);
export const errorRemovingPublicHoliday = createAction(
  REMOVE_PUBLIC_HOLIDAY_ERROR
);

export const UPDATE_PUBLIC_HOLIDAY = 'UPDATE_PUBLIC_HOLIDAY';
export const UPDATE_PUBLIC_HOLIDAY_SUCCESSFUL =
  'UPDATE_PUBLIC_HOLIDAY_SUCCESSFUL';
export const UPDATE_PUBLIC_HOLIDAY_ERROR = 'UPDATE_PUBLIC_HOLIDAY_ERROR';

export const updatePublicHoliday = createAction(UPDATE_PUBLIC_HOLIDAY);
export const updatePublicHolidaySuccessful = createAction(
  UPDATE_PUBLIC_HOLIDAY_SUCCESSFUL
);
export const updatePublicHolidayError = createAction(
  UPDATE_PUBLIC_HOLIDAY_ERROR
);

export const CREATE_PUBLIC_HOLIDAY = 'CREATE_PUBLIC_HOLIDAY';
export const CREATE_PUBLIC_HOLIDAY_SUCCESSFUL =
  'CREATE_PUBLIC_HOLIDAY_SUCCESSFUL';
export const CREATE_PUBLIC_HOLIDAY_ERROR = 'CREATE_PUBLIC_HOLIDAY_ERROR';

export const createPublicHoliday = createAction(CREATE_PUBLIC_HOLIDAY);
export const createPublicHolidaySuccessful = createAction(
  CREATE_PUBLIC_HOLIDAY_SUCCESSFUL
);
export const createPublicHolidayError = createAction(
  CREATE_PUBLIC_HOLIDAY_ERROR
);
