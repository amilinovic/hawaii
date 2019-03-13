import { push } from 'connected-react-router';
import { call, put, takeLatest } from 'redux-saga/effects';
import {
  errorReceivingTeam,
  errorRemovingTeam,
  receiveTeam,
  removeTeam,
  removeTeamSuccess,
  requestTeam,
  updateTeam,
  updateTeamError,
  updateTeamSuccessful
} from '../actions/teamActions';
import {
  createYear,
  createYearError,
  createYearSuccessful
} from '../actions/yearActions';
import {
  getTeamApi,
  removeTeamApi,
  updateTeamApi
} from '../services/teamService';
import { createYearApi } from '../services/yearService';
import {
  genericErrorHandler,
  withErrorHandling
} from './HOC/withErrorHandling';
import { toastrSuccess } from './toastrHelperSaga';

const getTeamSaga = function*(action) {
  const teamInformation = yield call(getTeamApi(action.payload));
  yield put(receiveTeam(teamInformation));
};

const removeTeamSaga = function*(action) {
  yield call(removeTeamApi(action.payload.id));
  yield put(removeTeamSuccess());
  yield put(toastrSuccess('Succesfully removed team'));
  yield put(push('/administration'));
};

const updateTeamSaga = function*(action) {
  yield call(updateTeamApi(action.payload));
  yield put(updateTeamSuccessful());
  yield put(toastrSuccess('Succesfully updated team'));
};

const createYearSaga = function*(action) {
  yield call(createYearApi(action.payload));
  yield put(createYearSuccessful());
  yield put(push('/administration'));
  yield put(toastrSuccess('Successfully created year'));
};

export const yearSaga = [
  takeLatest(
    requestTeam,
    withErrorHandling(getTeamSaga, genericErrorHandler(errorReceivingTeam))
  ),
  takeLatest(
    updateTeam,
    withErrorHandling(updateTeamSaga, genericErrorHandler(updateTeamError))
  ),
  takeLatest(
    removeTeam,
    withErrorHandling(removeTeamSaga, genericErrorHandler(errorRemovingTeam))
  ),
  takeLatest(
    createYear,
    withErrorHandling(createYearSaga, genericErrorHandler(createYearError))
  )
];
