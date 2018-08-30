import { call, put, takeLatest } from 'redux-saga/effects';
import {
  receiveTeams,
  requestTeams,
  errorReceivingTeams
} from '../actions/teamsActions';
import { getTeamsApi } from '../services/teamsService';

export const getAllTeams = function*() {
  try {
    const teamsInformation = yield call(getTeamsApi);
    yield put(receiveTeams(teamsInformation));
  } catch (error) {
    yield put(errorReceivingTeams(error));
  }
};

export const teamsSaga = [takeLatest(requestTeams, getAllTeams)];
