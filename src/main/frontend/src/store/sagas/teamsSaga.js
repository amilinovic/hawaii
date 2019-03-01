import { push } from 'connected-react-router';
import { call, put, takeLatest } from 'redux-saga/effects';
import {
  errorReceivingTeams,
  receiveTeams,
  requestTeams
} from '../actions/teamsActions';
import { getTeamsApi } from '../services/teamsService';

export const getAllTeams = function*() {
  try {
    const teamsInformation = yield call(getTeamsApi);
    yield put(receiveTeams(teamsInformation));
  } catch (error) {
    yield put(errorReceivingTeams(error));
    if (error.status === 401) {
      yield put(push('/login'));
    }
  }
};

export const teamsSaga = [takeLatest(requestTeams, getAllTeams)];
