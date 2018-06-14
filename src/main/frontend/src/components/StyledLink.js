import styled from 'styled-components';
import { prop, ifProp } from 'styled-tools';

export const StyledLink = styled.span`
  color: ${prop('color', 'white')};
  padding: 10px;
  border-radius: 5px;
  background: ${prop('background', '#FA4C50')};
  border: 1px solid #3c3c46;
  display: ${prop('display', 'inline-block')};
  &:before {
    content: '';
    background-image: url('${prop('icon', '')}');
    width: ${ifProp('icon', '20px', '')};
    height: ${ifProp('icon', '20px', '')};
    float: left;
    background-size: cover;
    margin-right: ${ifProp('icon', '10px', '')};
  }
`;
