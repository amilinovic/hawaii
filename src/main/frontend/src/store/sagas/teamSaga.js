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
  requestTeam
} from '../actions/teamActions';
import {
  createTeamApi,
  getTeamApi,
  removeTeamApi
} from '../services/teamService';

export const getTeamSaga = function*(action) {
  try {
    const teamInformation = yield call(getTeamApi, action.payload);
    yield put(receiveTeam(teamInformation));
  } catch (error) {
    yield put(errorReceivingTeam(error));
  }
};

export const removeTeamSaga = function*(action) {
  try {
    yield call(removeTeamApi, action.payload.id);
    yield put(removeTeamSuccess());
    yield put(push('/administration'));
  } catch (error) {
    yield put(errorRemovingTeam(error));
  }
};

export const createTeamSaga = function*(action) {
  try {
    yield call(createTeamApi, action.payload);
    yield put(createTeamSuccessful());
  } catch (error) {
    yield put(createTeamError(error));
  }
};

export const teamSaga = [
  takeLatest(requestTeam, getTeamSaga),
  takeLatest(createTeam, createTeamSaga),
  takeLatest(removeTeam, removeTeamSaga)
];
