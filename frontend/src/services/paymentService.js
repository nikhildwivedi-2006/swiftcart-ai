import axios from "axios";

const API = "/api/payments";

const paymentService = {

    createPaymentOrder: async(orderId)=>{

        const response = await axios.post(
            `${API}/create-order`,
            {
                orderId: orderId
            }
        );

        return response.data.data;
    },


    verifyPayment: async(data)=>{

        const response = await axios.post(
            `${API}/verify`,
            data
        );

        return response.data;
    }

};


export default paymentService;