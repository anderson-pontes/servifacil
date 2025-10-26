import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getUserAppointments, cancelAppointment } from '../services/appointmentService';
import reviewService from '../services/reviewService';
import { Calendar, Clock, X, Star } from 'lucide-react';
import UserNavigation from '../components/UserNavigation';
import ReviewModal from '../components/ReviewModal';

function MyAppointments() {
    const [appointments, setAppointments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [activeTab, setActiveTab] = useState('Pendente');
    const [showReviewModal, setShowReviewModal] = useState(false);
    const [selectedAppointment, setSelectedAppointment] = useState(null);
    const navigate = useNavigate();

    const tabs = ['Pendente', 'Concluido', 'Cancelado'];

    useEffect(() => {
        loadAppointments();
    }, [activeTab]);

    const loadAppointments = async () => {
        setLoading(true);
        const userId = localStorage.getItem('userId');
        const result = await getUserAppointments(userId, activeTab);
        if (result.success) {
            // Verificar quais agendamentos já foram avaliados
            const appointmentsWithReviewStatus = await Promise.all(
                result.data.map(async (appointment) => {
                    if (appointment.appointmentStatus === 'Concluido') {
                        try {
                            const canReview = await reviewService.canReviewAppointment(
                                appointment.appointmentId,
                                parseInt(userId)
                            );
                            return {
                                ...appointment,
                                canReview: canReview.data?.can_review || false,
                                reviewReason: canReview.data?.reason || ''
                            };
                        } catch (error) {
                            return { ...appointment, canReview: false };
                        }
                    }
                    return appointment;
                })
            );
            setAppointments(appointmentsWithReviewStatus);
        }
        setLoading(false);
    };

    const handleCancelAppointment = async (appointmentId) => {
        if (!confirm('Deseja realmente cancelar este agendamento?')) return;

        const userId = localStorage.getItem('userId');
        const result = await cancelAppointment(userId, appointmentId);
        if (result.success) {
            alert('Agendamento cancelado com sucesso!');
            loadAppointments();
        } else {
            alert(result.message);
        }
    };

    // Função para converter data do formato dd/MM/yyyy HH:mm para Date
    const parseDate = (dateString) => {
        if (!dateString) return null;

        // Se já é um objeto Date válido
        if (dateString instanceof Date) return dateString;

        // Se está no formato ISO (2025-10-23T15:00:00)
        if (dateString.includes('T')) {
            return new Date(dateString);
        }

        // Se está no formato brasileiro (23/10/2025 15:00)
        const [datePart, timePart] = dateString.split(' ');
        const [day, month, year] = datePart.split('/');
        const [hours, minutes] = timePart ? timePart.split(':') : ['00', '00'];

        return new Date(year, month - 1, day, hours, minutes);
    };

    const formatDate = (dateString) => {
        const date = parseDate(dateString);
        if (!date || isNaN(date.getTime())) return 'Data inválida';

        return date.toLocaleDateString('pt-BR', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
        });
    };

    const formatTime = (dateString) => {
        const date = parseDate(dateString);
        if (!date || isNaN(date.getTime())) return '--:--';

        return date.toLocaleTimeString('pt-BR', {
            hour: '2-digit',
            minute: '2-digit',
        });
    };

    const handleOpenReviewModal = async (appointment) => {
        // Buscar nome do profissional do serviço
        let professionalName = 'Profissional';

        if (appointment.service?.professional?.userName) {
            professionalName = appointment.service.professional.userName;
        }

        setSelectedAppointment({
            ...appointment,
            professionalName
        });
        setShowReviewModal(true);
    };

    const handleSubmitReview = async (rating, comment) => {
        try {
            const userId = localStorage.getItem('userId');
            const result = await reviewService.createReview(
                selectedAppointment.appointmentId,
                parseInt(userId),
                rating,
                comment
            );

            if (result.success) {
                alert('Avaliação enviada com sucesso!');
                setShowReviewModal(false);
                loadAppointments();
            }
        } catch (error) {
            throw error;
        }
    };

    return (
        <div className="min-h-screen bg-gray-50">
            {/* Header com Navegação */}
            <UserNavigation />

            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {/* Título da Página */}
                <h1 className="text-3xl font-bold text-gray-800 mb-6">Meus Agendamentos</h1>
                {/* Tabs */}
                <div className="flex gap-2 mb-6 overflow-x-auto">
                    {tabs.map((tab) => (
                        <button
                            key={tab}
                            onClick={() => setActiveTab(tab)}
                            className={`px-6 py-2 rounded-lg whitespace-nowrap transition ${activeTab === tab
                                ? 'bg-yellow-400 text-white'
                                : 'bg-white text-gray-700 hover:bg-gray-100'
                                }`}
                        >
                            {tab}
                        </button>
                    ))}
                </div>

                {/* Appointments List */}
                {loading ? (
                    <div className="text-center py-12">
                        <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-violet-400"></div>
                        <p className="mt-4 text-gray-600">Carregando agendamentos...</p>
                    </div>
                ) : appointments.length === 0 ? (
                    <div className="text-center py-12 bg-white rounded-lg">
                        <Calendar className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                        <p className="text-gray-600 text-lg">Nenhum agendamento encontrado</p>
                    </div>
                ) : (
                    <div className="space-y-4">
                        {appointments.map((appointment) => (
                            <div
                                key={appointment.appointmentId}
                                className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition"
                            >
                                <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
                                    <div className="flex-1">
                                        <h3 className="text-xl font-bold text-gray-800 mb-2">
                                            {appointment.service?.title || 'Serviço'}
                                        </h3>

                                        <div className="space-y-2">
                                            <div className="flex items-center gap-2 text-gray-600">
                                                <Calendar className="w-4 h-4" />
                                                <span>
                                                    {formatDate(appointment.startDate)} - {formatDate(appointment.endDate)}
                                                </span>
                                            </div>

                                            <div className="flex items-center gap-2 text-gray-600">
                                                <Clock className="w-4 h-4" />
                                                <span>
                                                    {formatTime(appointment.startDate)} - {formatTime(appointment.endDate)}
                                                </span>
                                            </div>
                                        </div>

                                        <div className="mt-3">
                                            <span
                                                className={`inline-block px-3 py-1 rounded-full text-sm ${appointment.appointmentStatus === 'Pendente'
                                                    ? 'bg-yellow-100 text-yellow-800'
                                                    : appointment.appointmentStatus === 'Concluido'
                                                        ? 'bg-green-100 text-green-800'
                                                        : 'bg-red-100 text-red-800'
                                                    }`}
                                            >
                                                {appointment.appointmentStatus}
                                            </span>
                                        </div>
                                    </div>

                                    {appointment.appointmentStatus === 'Pendente' && (
                                        <div className="flex gap-2">
                                            <button
                                                onClick={() => handleCancelAppointment(appointment.appointmentId)}
                                                className="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition flex items-center gap-2"
                                            >
                                                <X className="w-4 h-4" />
                                                Cancelar
                                            </button>
                                        </div>
                                    )}

                                    {appointment.appointmentStatus === 'Concluido' && (
                                        <>
                                            {appointment.canReview === false && appointment.reviewReason ? (
                                                <div className="px-4 py-2 bg-gray-100 text-gray-600 rounded-lg flex items-center gap-2">
                                                    <Star className="w-4 h-4 fill-yellow-400 text-yellow-400" />
                                                    Já Avaliado
                                                </div>
                                            ) : (
                                                <button
                                                    onClick={() => handleOpenReviewModal(appointment)}
                                                    className="px-4 py-2 bg-yellow-400 text-white rounded-lg hover:bg-yellow-500 transition flex items-center gap-2"
                                                >
                                                    <Star className="w-4 h-4" />
                                                    Avaliar Serviço
                                                </button>
                                            )}
                                        </>
                                    )}
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            {/* Modal de Avaliação */}
            {showReviewModal && selectedAppointment && (
                <ReviewModal
                    appointment={{
                        serviceName: selectedAppointment.service?.title || 'Serviço',
                        professionalName: selectedAppointment.professionalName || selectedAppointment.service?.professional?.userName || 'Profissional'
                    }}
                    onClose={() => setShowReviewModal(false)}
                    onSubmit={handleSubmitReview}
                />
            )}
        </div>
    );
}

export default MyAppointments;
