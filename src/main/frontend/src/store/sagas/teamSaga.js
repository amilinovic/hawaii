import { push } from 'connected-react-router';
import { call, put, takeLatest } from 'redux-saga/effects';
import {
  createTeam,
  createTeamError,
  createTeamSuccessful,
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
  createTeamApi,
  getTeamApi,
  removeTeamApi,
  updateTeamApi
} from '../services/teamService';
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

const createTeamSaga = function*(action) {
  yield call(createTeamApi(action.payload));
  yield put(createTeamSuccessful());
  yield put(push('/administration'));
  yield put(toastrSuccess('Successfully created team'));
};

export const teamSaga = [
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
    createTeam,
    withErrorHandling(createTeamSaga, genericErrorHandler(createTeamError))
  )
];
