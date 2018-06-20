import React, { Component } from 'react';
import { Container, Row, Col } from 'reactstrap';
import { UserImage } from '../common/UserImage';
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
    return (
      <Container fluid>
        <ShadowRow className="py-2">
          <Col className="d-flex align-items-center">
            <UserImage
              image={
                this.props.store.fetching === ''
                  ? 'none'
                  : this.props.store.employeeInformation.results[0].picture
                      .large
              }
              size="100px"
            />
            {this.props.store.fetching === '' ? (
              <span>Fetching...</span>
            ) : (
              <div>
                <h3>
                  {this.props.store.employeeInformation.results[0].name.first}
                </h3>
                <h5>Web developer</h5>
                <h5>{this.props.store.employeeInformation.results[0].email}</h5>
              </div>
            )}
          </Col>
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
