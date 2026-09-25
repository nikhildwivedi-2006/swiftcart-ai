import api from "./api"; 

const API = "/payments";

const paymentService = {

    createPaymentOrder: async(orderId)=>{

        const response = await api.post(
            `${API}/create-order`,
            {
                orderId: orderId
            }
        );

        return response.data.data;
    },


    verifyPayment: async(data)=>{

        const response = await api.post(
            `${API}/verify`,
            data
        );

        return response.data;
    }

};


export default paymentService;