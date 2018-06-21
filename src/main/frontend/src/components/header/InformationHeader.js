import React, { Component } from 'react';
import { Container, Row, Col } from 'reactstrap';
import UserInfoComponent from '../UserInfoExtended';
import { ShadowRow } from '../common/ShadowRow';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { requestApiData } from '../../store/actions/EmployeesActions';

class InformationHeader extends Component {
  componentDidMount() {
    this.props.requestApiData();
  }

  render() {
    // TODO change mocked data with actual data
    console.log(this.props);
    return (
      <Container fluid>
        <ShadowRow className="py-2">
          {this.props.store.fetching === '' ? null : (
            <UserInfoComponent
              employeeInfo={this.props.store.employeeInformation.results[0]}
            />
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

const mapStateToProps = state => ({ store: state.employeeInformation });

const mapDispatchToProps = dispatch =>
  bindActionCreators({ requestApiData }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(InformationHeader);
