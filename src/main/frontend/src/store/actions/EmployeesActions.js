import { createAction } from 'redux-actions';

export const REQUEST_API_DATA = 'REQUEST_API_DATA';
export const RECEIVE_API_DATA = 'RECEIVE_API_DATA';
// export const RECEIVE_API_DATA_ERROR = 'RECEIVE_API_DATA_ERROR';

export const requestApiData = createAction('REQUEST_API_DATA');
export const errorReceivingApiData = createAction('RECEIVE_API_DATA_ERROR');
// export const receiveApiData = createAction('RECEIVE_API_DATA');

// console.log(receiveApiData)

// export const errorReceivingApiData = error => ({
//   payload: {
//     error
//   },
//   type: RECEIVE_API_DATA_ERROR
// });

// export const requestApiData = () => ({
//   type: REQUEST_API_DATA
// });

export const receiveApiData = createAction('RECEIVE_API_DATA');
