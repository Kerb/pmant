import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Mic, Upload, Calendar, CheckSquare, Clock, ChevronRight } from "lucide-react";
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";

const Dashboard = () => {

  const focusBlocks = [
    { id: 1, task: "Review Q4 Roadmap", time: "10:00-10:25", pomodoro: "1/3" },
    { id: 2, task: "Prepare Presentation", time: "11:00-11:25", pomodoro: "1/2" },
  ];

  const actionItems = [
    { id: 1, title: "Send follow-up email to stakeholders", project: "Marketing", due: "Today", priority: "high" },
    { id: 2, title: "Update Trello board with new tasks", project: "Operations", due: "Tomorrow", priority: "medium" },
    { id: 3, title: "Schedule team retrospective", project: "Team", due: "This week", priority: "low" },
  ];
  const navigate = useNavigate();
  const [user, setUser] = useState({});
  const [todaysMeetings, setTodaysMeetings] = useState([]);
  const [isLoading, setIsLoading] = useState(false); // 👈 для статуса загрузки

  // 🔹 Функция загрузки списка записей
  const loadRecordings = () => {
    setIsLoading(true);
    fetch('/api/recordings', {
      method: 'POST',
      credentials: 'include'
    })
        .then(res => res.json())
        .then(data => {
          if (data.success) {
            setTodaysMeetings(data.recordings || []);
            console.log('Fetched todaysMeetings:', data.recordings);
          } else {
            console.error('Error fetching recordings:', data);
          }
        })
        .catch(err => console.error('Error loading recordings:', err))
        .finally(() => setIsLoading(false));
  };


  useEffect(() => {
    fetch('/api/me', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include'
    })
        .then(response => response.json())
        .then(data => {
          if (data.success) {
            console.log('Got my data in session: ', data);
            setUser({...data});
            loadRecordings();

            // fetch('/api/recordings', {
            //   method: 'POST',
            //   credentials: 'include'
            // })
            //     .then(response => response.json())
            //     .then(recordingsData => {
            //       if (recordingsData.success) {
            //         setTodaysMeetings(recordingsData.recordings || []);
            //         console.log('Fetched todaysMeetings:', recordingsData.recordings);
            //       } else {
            //         console.log('Error fetching recordings:', recordingsData);
            //       }
            //     })
            //     .catch(err => {
            //       console.error('Error loading recordings:', err);
            //     });

          } else {
            navigate("/login")
            console.log('No my data in session, redirect to login');
          }
        })
        .catch(error => {
          console.log('Error during logout', error);
          navigate("/error") //todo реализовать страницу ошибок
        });
  }, []);

  return (
    <div className="min-h-screen bg-background">
      {/* Header */}
      <header className="border-b border-border bg-card/50 backdrop-blur-sm sticky top-0 z-10">
        <div className="container mx-auto px-4 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-primary to-primary/80 flex items-center justify-center">
                <span className="text-2xl">🦗</span>
              </div>
              <div>
                <h1 className="text-2xl font-bold">PMantis</h1>
                <p className="text-sm text-muted-foreground">AI Meeting Assistant</p>
              </div>
            </div>
            <div className="flex items-center gap-2">
              <Button variant="outline" size="sm">
                <Calendar className="w-4 h-4 mr-2" />
                Calendar
              </Button>
              <Button variant="outline" size="sm">
                Settings
              </Button>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="container mx-auto px-4 py-8">
        {/* Quick Actions */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-8">
          <Card className="p-6 hover:shadow-lg transition-all cursor-pointer border-2 border-primary/20 bg-gradient-to-br from-primary/5 to-transparent">
            <div className="flex items-start gap-4">
              <div className="w-12 h-12 rounded-lg bg-primary/10 flex items-center justify-center">
                <Mic className="w-6 h-6 text-primary" />
              </div>
              <div className="flex-1">
                <h3 className="text-lg font-semibold mb-1">Record Meeting</h3>
                <p className="text-sm text-muted-foreground mb-3">Start live recording or upload audio</p>
                <Button className="w-full sm:w-auto">
                  Start Recording
                </Button>
              </div>
            </div>
          </Card>

          <Card className="p-6 hover:shadow-lg transition-all cursor-pointer border-2 border-border">
            <div className="flex items-start gap-4">
              <div className="w-12 h-12 rounded-lg bg-muted flex items-center justify-center">
                <Upload className="w-6 h-6 text-foreground" />
              </div>
              <div className="flex-1">
                <h3 className="text-lg font-semibold mb-1">Import Audio</h3>
                <p className="text-sm text-muted-foreground mb-3">Upload from files or voice memos</p>
                <input 
                  type="file" 
                  id="audio-upload" 
                  className="hidden" 
                  accept="audio/*" 
                  onChange={(e) => {
                    const file = e.target.files?.[0];
                    if (file) {
                      const formData = new FormData();
                      formData.append('file', file);

                      fetch('/api/createRecording', {
                        method: 'POST',
                        body: formData,
                        credentials: 'include'
                      })
                          .then(response => response.json())
                          .then(data => {
                            if (data.success) {
                              console.log('Success:', data);
                              loadRecordings();
                            } else {
                              console.log('Error white uploading file:', data);
                            }
                          })
                          .catch(error => {
                            console.error('Error:', error);
                          })
                          .finally(() => {
                            // Сброс значения инпута, чтобы можно было выбрать тот же файл снова
                            e.target.value = '';
                          });;
                    }
                  }}
                />
                <Button 
                  variant="outline" 
                  className="w-full sm:w-auto"
                  onClick={() => document.getElementById('audio-upload')?.click()}>
                  Choose File
                </Button>
              </div>
            </div>
          </Card>
        </div>

        {/* Today's Overview */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-8">
          {/* Meetings */}
          <Card className="p-6">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-lg font-semibold flex items-center gap-2">
                <Calendar className="w-5 h-5 text-primary" />
                Today's Meetings
              </h2>
              <Badge variant="secondary">{todaysMeetings.length}</Badge>
            </div>
            <div className="space-y-3">
              {todaysMeetings.map((meeting) => (
                <div key={meeting.id} className="p-3 rounded-lg bg-muted/50 hover:bg-muted transition-colors cursor-pointer">
                  <div className="flex items-center justify-between mb-1">
                    <span className="font-medium text-sm">{meeting.title}</span>
                    {meeting.status === "transcribing" && (
                      <Badge variant="outline" className="text-xs">Transcribing...</Badge>
                    )}
                  </div>
                  <div className="flex items-center gap-2 text-xs text-muted-foreground">
                    <Clock className="w-3 h-3" />
                    {meeting.time}
                  </div>
                </div>
              ))}
            </div>
            <Button variant="ghost" className="w-full mt-3" size="sm">
              View All <ChevronRight className="w-4 h-4 ml-1" />
            </Button>
          </Card>

          {/* Focus Blocks */}
          <Card className="p-6">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-lg font-semibold flex items-center gap-2">
                <Clock className="w-5 h-5 text-primary" />
                Focus Blocks
              </h2>
              <Badge variant="secondary">Pomodoro</Badge>
            </div>
            <div className="space-y-3">
              {focusBlocks.map((block) => (
                <div key={block.id} className="p-3 rounded-lg bg-primary/5 border border-primary/20">
                  <div className="flex items-start justify-between mb-1">
                    <span className="font-medium text-sm">{block.task}</span>
                    <Badge variant="outline" className="text-xs bg-primary/10">{block.pomodoro}</Badge>
                  </div>
                  <div className="text-xs text-muted-foreground">{block.time}</div>
                </div>
              ))}
            </div>
            <Button variant="ghost" className="w-full mt-3" size="sm">
              Plan My Day <ChevronRight className="w-4 h-4 ml-1" />
            </Button>
          </Card>

          {/* Action Items */}
          <Card className="p-6">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-lg font-semibold flex items-center gap-2">
                <CheckSquare className="w-5 h-5 text-primary" />
                Next Actions
              </h2>
              <Badge variant="secondary">{actionItems.length}</Badge>
            </div>
            <div className="space-y-3">
              {actionItems.map((item) => (
                <div key={item.id} className="p-3 rounded-lg bg-muted/50 hover:bg-muted transition-colors cursor-pointer">
                  <div className="flex items-start gap-2 mb-2">
                    <div className="w-4 h-4 rounded border-2 border-primary mt-0.5 flex-shrink-0" />
                    <div className="flex-1">
                      <p className="font-medium text-sm mb-1">{item.title}</p>
                      <div className="flex items-center gap-2 flex-wrap">
                        <Badge variant="outline" className="text-xs">{item.project}</Badge>
                        <span className="text-xs text-muted-foreground">{item.due}</span>
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>
            <Button variant="ghost" className="w-full mt-3" size="sm">
              View All Tasks <ChevronRight className="w-4 h-4 ml-1" />
            </Button>
          </Card>
        </div>

        {/* Stats */}
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          <Card className="p-4">
            <div className="text-2xl font-bold text-primary mb-1">12</div>
            <div className="text-sm text-muted-foreground">Meetings This Week</div>
          </Card>
          <Card className="p-4">
            <div className="text-2xl font-bold text-primary mb-1">24</div>
            <div className="text-sm text-muted-foreground">Active Tasks</div>
          </Card>
          <Card className="p-4">
            <div className="text-2xl font-bold text-primary mb-1">8.5h</div>
            <div className="text-sm text-muted-foreground">Focus Time</div>
          </Card>
          <Card className="p-4">
            <div className="text-2xl font-bold text-primary mb-1">95%</div>
            <div className="text-sm text-muted-foreground">Completion Rate</div>
          </Card>
        </div>
      </main>
    </div>
  );
};

export default Dashboard;
