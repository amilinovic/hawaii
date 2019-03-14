import { call, put, takeLatest } from 'redux-saga/effects';
import {
  errorReceivingAllowance,
  receiveAllowance,
  requestAllowance
} from '../actions/allowanceActions';
import { getAllowanceApi } from '../services/allowanceService';
import {
  genericErrorHandler,
  withErrorHandling
} from './HOC/withErrorHandling';

export const getAllowancesSaga = function*(action) {
  const allowance = yield call(getAllowanceApi(action.payload));
  yield put(receiveAllowance(allowance));
};

export const allowanceSaga = [
  takeLatest(
    requestAllowance,
    withErrorHandling(
      getAllowancesSaga,
      genericErrorHandler(errorReceivingAllowance)
    )
  )
];
