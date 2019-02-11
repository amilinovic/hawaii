import { push } from 'connected-react-router';
import { call, put, takeLatest } from 'redux-saga/effects';
import {
  errorReceivingTeam,
  errorRemovingTeam,
  receiveTeam,
  removeTeam,
  removeTeamSuccess,
  requestTeam
} from '../actions/teamActions';
import { getTeamApi, removeTeamApi } from '../services/teamService';

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

export const teamSaga = [
  takeLatest(requestTeam, getTeamSaga),
  takeLatest(removeTeam, removeTeamSaga)
];
