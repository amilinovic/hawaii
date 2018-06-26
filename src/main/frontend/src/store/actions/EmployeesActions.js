export const REQUEST_API_DATA = 'REQUEST_API_DATA';
export const RECEIVE_API_DATA = 'RECEIVE_API_DATA';

export const errorReceivingApiData = error => ({
  payload: {
    error
  },
  type: REQUEST_API_DATA
});

export const requestApiData = () => ({
  type: REQUEST_API_DATA
});
export const receiveApiData = employeeInformation => ({
  type: RECEIVE_API_DATA,
  employeeInformation
});
