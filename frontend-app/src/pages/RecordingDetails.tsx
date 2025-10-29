import {useState, useEffect} from "react";
import {useParams, useNavigate} from "react-router-dom";
import {Button} from "@/components/ui/button";
import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card";
import {Badge} from "@/components/ui/badge";
import {ChevronLeft, Download, Calendar, Clock, FileText} from "lucide-react";

const RecordingDetails = () => {
    const {id} = useParams();
    const navigate = useNavigate();
    const [recording, setRecording] = useState<any>(null);
    const [loading, setLoading] = useState(true);

    // Mock data for demonstration
    // In a real implementation, this would come from an API call
    useEffect(() => {
        fetch(`/api/recording-details/${id}`, {
            method: 'POST',
            credentials: 'include'
        }).then(res => res.json())
            .then(data => {
                if (data.success) {
                    console.log('✅ Meeting details:', data);
                    setRecording({...data.meetingDetails});
                } else {
                    console.error('❌ failed to get data');
                }
            })
            .catch(error => {
                console.log('Error', error); //todo реализовать страницу ошибок
            })
            .finally(() => {
                setLoading(false);
            });
    }, [id]);

    if (loading) {
        return (
            <div className="min-h-screen bg-background flex items-center justify-center">
                <div className="text-center">
                    <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto"></div>
                    <p className="mt-4 text-muted-foreground">Loading recording details...</p>
                </div>
            </div>
        );
    }

    if (!recording) {
        return (
            <div className="min-h-screen bg-background flex items-center justify-center">
                <div className="text-center">
                    <h2 className="text-2xl font-bold mb-2">Recording Not Found</h2>
                    <p className="text-muted-foreground mb-4">The requested recording could not be found.</p>
                    <Button onClick={() => navigate("/dashboard")}>
                        <ChevronLeft className="w-4 h-4 mr-2"/>
                        Back to Dashboard
                    </Button>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-background">
            {/* Header */}
            <header className="border-b border-border bg-card/50 backdrop-blur-sm sticky top-0 z-10">
                <div className="container mx-auto px-4 py-4">
                    <div className="flex items-center justify-between">
                        <div className="flex items-center gap-3">
                            <Button variant="ghost" size="sm" onClick={() => navigate("/dashboard")}>
                                <ChevronLeft className="w-4 h-4 mr-2"/>
                                Back
                            </Button>
                            <div>
                                <h1 className="text-xl font-bold">Meeting record: {recording.title}</h1>
                                <div className="flex items-center gap-4 text-sm text-muted-foreground">
                                    <div className="flex items-center gap-1">
                                        <Calendar className="w-4 h-4"/>
                                        {recording.date}
                                    </div>
                                    <div className="flex items-center gap-1">
                                        <Clock className="w-4 h-4"/>
                                        {recording.time} ({recording.duration})
                                    </div>
                                    <Badge variant={recording.status === "processed" ? "default" : "secondary"}>
                                        {recording.status}
                                    </Badge>
                                </div>
                            </div>
                        </div>
                        <div className="flex items-center gap-2">
                            <Button variant="outline" size="sm">
                                <Download className="w-4 h-4 mr-2"/>
                                Export
                            </Button>
                        </div>
                    </div>
                </div>
            </header>

            {/* Main Content */}
            <main className="container mx-auto px-4 py-8">
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                    {/* Left Column - Minutes of Meeting (MoM) */}
                    <div className="space-y-6">
                        <Card>
                            <CardHeader>
                                <CardTitle className="flex items-center gap-2">
                                    <FileText className="w-5 h-5"/>
                                    Minutes of Meeting
                                </CardTitle>
                            </CardHeader>
                            <CardContent>
                                <div className="prose max-w-none">
                                    <pre className="whitespace-pre-wrap font-sans">
                                        {recording.mom}
                                    </pre>
                                </div>
                            </CardContent>
                        </Card>
                    </div>

                    {/* Right Column - Full Transcript */}
                    <div className="space-y-6">
                        <Card>
                            <CardHeader>
                                <CardTitle>Full Transcript</CardTitle>
                            </CardHeader>
                            <CardContent>
                                <div className="max-h-[calc(100vh-200px)] overflow-y-auto">
                                  <pre className="whitespace-pre-wrap font-sans text-sm">
                                    {JSON.parse(recording.speech).textƒ}
                                  </pre>
                                </div>
                            </CardContent>
                        </Card>
                    </div>
                </div>
            </main>
        </div>
    );
};

export default RecordingDetails;
