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
  getTeamApi,
  removeTeamApi,
  updateTeamApi
} from '../services/teamService';

export const getTeamSaga = function*(action) {
  try {
    const teamInformation = yield call(getTeamApi(action.payload));
    yield put(receiveTeam(teamInformation));
  } catch (error) {
    yield put(errorReceivingTeam(error));
    if (error.status === 401) {
      yield put(push('/login'));
    }
  }
};

export const removeTeamSaga = function*(action) {
  try {
    yield call(removeTeamApi(action.payload.id));
    yield put(removeTeamSuccess());
    yield put(push('/administration'));
  } catch (error) {
    yield put(errorRemovingTeam(error));
  }
};

export const updateTeamSaga = function*(action) {
  try {
    yield call(updateTeamApi(action.payload));
    yield put(updateTeamSuccessful());
  } catch (error) {
    yield put(updateTeamError(error));
  }
};

export const teamSaga = [
  takeLatest(requestTeam, getTeamSaga),
  takeLatest(updateTeam, updateTeamSaga),
  takeLatest(removeTeam, removeTeamSaga)
];
