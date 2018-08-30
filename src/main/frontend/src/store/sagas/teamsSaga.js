import { call, put, takeLatest } from 'redux-saga/effects';
import * as actions from '../actions/teamsActions';
import { getTeamsApi, createTeamApi } from '../services/teamsService';

export const getAllTeams = function*() {
  try {
    const teamsInformation = yield call(getTeamsApi);
    yield put(actions.receiveTeams(teamsInformation));
  } catch (error) {
    yield put(actions.errorReceivingTeams(error));
  }
};

export const createTeam = function*() {
  try {
    const team = yield call(createTeamApi());
    yield put(actions.createdTeam(team));
  } catch (error) {
    yield put(actions.creatingTeamError(error));
  }
};

export const teamsSaga = [takeLatest(actions.requestTeams, getAllTeams)];
export const createTeamSaga = [takeLatest(actions.creatingTeam, createTeam)];
