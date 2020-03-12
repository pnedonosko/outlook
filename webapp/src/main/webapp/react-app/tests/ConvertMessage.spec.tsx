import * as React from "react";
import ConvertMessage from "../containers/ConvertMessage";
import { ConvertMessageTypes } from "../containers/ConvertMessage/convert-message.types";
import { shallow, mount } from "enzyme";
import { convertToActivityConfig } from "../containers/ConvertMessage/convert-message.config";
import { Translation } from "react-i18next";
import axios, { AxiosResponse } from "axios";
jest.mock("axios");

describe("<ConvertMessage />", () => {
  let wrapper;
  const mockedAxios = axios as jest.Mocked<typeof axios>;

  beforeEach(() => {
    wrapper = shallow(
      <ConvertMessage type={ConvertMessageTypes.Activity} userName="user_name" userUrl="userUrl" />
    );
  });

  it("should render <Translation /> after recieving config", () => {
    wrapper.setState({ config: convertToActivityConfig });
    expect(wrapper.find(Translation)).toHaveLength(1);
  });

  /* it might be good to add end-to-end tests (for ex with Cypress framework) for project to test real request execution */
  it("should display messageBar with success after successfull request", () => {
    const promise: Promise<AxiosResponse> = new Promise(resolve =>
      setTimeout(
        () =>
          resolve({
            data: {
              _links: {
                parent: { href: "userUrl/user_name" },
                self: {
                  href:
                    "userUrl/user_name/activity?messageId={messageId}&title={title}&subject={subject}&body={body}&created={created}&modified={modified}&userName={userName}&userEmail={userEmail}&fromName={fromName}&fromEmail={fromEmail}",
                  templated: true
                }
              },
              activityStatus: {
                link: "/portal/intranet/activity?id=41",
                space: false,
                targetName: "user_name"
              }
            },
            status: 200,
            statusText: "OK",
            config: {},
            headers: {}
          }),
        100
      )
    );
    mockedAxios.get.mockResolvedValue(promise);
    const wrapper = mount(
      <ConvertMessage type={ConvertMessageTypes.Activity} userName="user_name" userUrl="userUrl" />
    );
    expect(wrapper.find("MessageBar").length).toEqual(0);
    promise.then(() => {
      expect(wrapper.find("MessageBar").length).toEqual(1);
      mockedAxios.get.mockClear();
    });
  });

  it("should display messageBar with error after failed request", () => {
    const promise = new Promise((_, reject) =>
      setTimeout(() =>
        reject({
          code: 400,
          essage: "Required String parameter 'messageId' is not present"
        })
      )
    );
    mockedAxios.get.mockResolvedValue(promise);
    const wrapper = mount(
      <ConvertMessage type={ConvertMessageTypes.Activity} userName="user_name" userUrl="userUrl" />
    );
    expect(wrapper.find("MessageBar").length).toEqual(0);
    promise.then(() => {
      expect(wrapper.find("MessageBar").length).toEqual(1);
      mockedAxios.get.mockClear();
    });
  });
});
