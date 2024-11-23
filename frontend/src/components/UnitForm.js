import React from "react";
import { Modal, Form, Input, InputNumber, Button, message } from "antd";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const UnitForm = ({ visible, setShowNewUnitModal, units, setUnits }) => {
    const [newUnitForm] = Form.useForm();
    const navigate = useNavigate();

    const addNewUnit = async (unitValues) => {
        try {
            const token = localStorage.getItem("accessToken");

            if (!token) {
                message.error("User not authenticated");
                navigate("/login");
                return;
            }

            const response = await axios.post(
                "http://localhost:8080/units",
                {
                    name: unitValues.name,
                    abbreviation: unitValues.abbreviation,
                    conversionToGrams: unitValues.conversionToGrams,
                },
                {
                    headers: { Authorization: `Bearer ${token}` },
                }
            );

            if (response.status === 200 || response.status === 201) {
                setUnits([...units, response.data]);
                message.success("New unit added successfully!");
                setShowNewUnitModal(false);
                newUnitForm.resetFields();
            } else {
                message.error("Unexpected response from server");
            }
        } catch (error) {
            message.error("Failed to add new unit");
        }
    };

    return (
        <Modal
            title="Add New Unit"
            open={visible}
            onCancel={() => setShowNewUnitModal(false)}
            footer={null}
        >
            <Form form={newUnitForm} layout="vertical" onFinish={addNewUnit}>
                <Form.Item
                    name="name"
                    label="Unit Name"
                    rules={[{ required: true, message: "Please enter the unit name" }]}
                >
                    <Input placeholder="Enter unit name" />
                </Form.Item>

                <Form.Item
                    name="abbreviation"
                    label="Abbreviation"
                    rules={[{ required: true, message: "Please enter the abbreviation" }]}
                >
                    <Input placeholder="Enter abbreviation (e.g. kg)" />
                </Form.Item>

                <Form.Item
                    name="conversionToGrams"
                    label="Conversion to Grams"
                    rules={[{ required: true, message: "Please enter conversion to grams" }]}
                >
                    <InputNumber placeholder="Enter conversion value" style={{ width: "100%" }} />
                </Form.Item>

                <Form.Item>
                    <Button type="primary" htmlType="submit">
                        Add Unit
                    </Button>
                </Form.Item>
            </Form>
        </Modal>
    );
};

export default UnitForm;
