import React, { Component } from 'react';
import { Container, Row, Col } from 'reactstrap';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import UserInfoComponent from '../UserInfoExtended';
import { ShadowRow } from '../common/shadowRow';
import { requestApiData } from '../../store/actions/randomUserApiActions';
import { getEmployee, getFetching } from '../../store/selectors';

class InformationHeader extends Component {
  componentDidMount() {
    this.props.requestApiData();
  }

  render() {
    // TODO change mocked data with actual data
    return (
      <Container fluid>
        <ShadowRow className="py-2">
          {this.props.fetching === '' ? null : (
            <UserInfoComponent employeeInfo={this.props.employee} />
          )}
          <Col className="flex-column d-none d-xl-flex">
            <Row className="h-100 align-items-center">
              <Col>Team</Col>
            </Row>
            <Row className="h-100 align-items-center">
              <Col>Hr manager</Col>
            </Row>
          </Col>
        </ShadowRow>
      </Container>
    );
  }
}

const mapStateToProps = state => ({
  employee: getEmployee(state),
  fetching: getFetching(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ requestApiData }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(InformationHeader);
