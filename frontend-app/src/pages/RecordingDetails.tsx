import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Separator } from "@/components/ui/separator";
import { ChevronLeft, Download, Calendar, Clock, FileText } from "lucide-react";

const RecordingDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [recording, setRecording] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  // Mock data for demonstration
  // In a real implementation, this would come from an API call
  useEffect(() => {
    // Simulate API call
    setTimeout(() => {
      setRecording({
        id,
        title: "Project Planning Meeting",
        date: "2025-10-28",
        time: "14:00 - 15:30",
        duration: "1h 30m",
        status: "processed",
        mom: `
# Project Planning Meeting - MoM

## Attendees
- John Smith (Project Manager)
- Sarah Johnson (Designer)
- Michael Lee (Developer)
- Emma Wilson (Client)

## Agenda
1. Project scope review
2. Timeline discussion
3. Resource allocation
4. Next steps

## Key Decisions
- Finalized project scope with all stakeholders
- Agreed on timeline with milestones
- Assigned team members to specific tasks
- Scheduled next meeting for November 5th

## Action Items
| Task | Owner | Due Date |
|------|-------|----------|
| Create wireframes | Sarah | Nov 2 |
| Set up development environment | Michael | Nov 1 |
| Prepare project documentation | John | Nov 3 |
| Review budget allocation | Emma | Nov 5 |
        `,
        transcript: `
[14:00] John Smith: Hello everyone, thanks for joining today. Let's start with reviewing the project scope we discussed last week.

[14:01] Sarah Johnson: I've gone through the requirements document and have some questions about the user interface expectations.

[14:02] Michael Lee: Yes, I also have some technical concerns regarding the integration with the existing system.

[14:03] Emma Wilson: I'm happy to clarify both of those points. For the UI, we're looking for something clean and modern, similar to what we discussed in our initial meetings.

[14:05] John Smith: Great. Let's make sure we document all these points. Sarah, can you start working on some wireframes based on our discussion?

[14:06] Sarah Johnson: Absolutely. I'll have a first version ready by next Tuesday for review.

[14:07] Michael Lee: Regarding the technical integration, I think we should use the new API framework that was recently released. It would make our implementation much cleaner.

[14:09] John Smith: Sounds good. Let's make sure we're all aligned on the timeline. I've prepared a draft schedule, let me share it with you.

[14:10] Emma Wilson: Before we move on, I want to confirm the budget allocation for the additional features we discussed.

[14:12] John Smith: Good point. I'll prepare a detailed breakdown and send it to you by tomorrow.

[14:15] Sarah Johnson: I have a meeting with the design team tomorrow morning. Should I bring up any specific points regarding the project branding?

[14:16] Emma Wilson: Yes, please make sure they're aware of our color scheme preferences and brand guidelines.

[14:20] Michael Lee: I'll start setting up the development environment today. Is there anything specific I should know about the deployment process?

[14:22] John Smith: I'll connect you with our DevOps team. They can provide you with all the necessary documentation.

... (meeting continues) ...

[15:25] John Smith: Let's wrap up for today. We'll schedule the next meeting for November 5th. I'll send out a calendar invite.

[15:28] Emma Wilson: Thank you all for your time. I'm looking forward to seeing the progress.

[15:30] John Smith: Thanks everyone. Have a great day!
        `
      });
      setLoading(false);
    }, 500);
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
            <ChevronLeft className="w-4 h-4 mr-2" />
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
                <ChevronLeft className="w-4 h-4 mr-2" />
                Back
              </Button>
              <div>
                <h1 className="text-xl font-bold">{recording.title}</h1>
                <div className="flex items-center gap-4 text-sm text-muted-foreground">
                  <div className="flex items-center gap-1">
                    <Calendar className="w-4 h-4" />
                    {recording.date}
                  </div>
                  <div className="flex items-center gap-1">
                    <Clock className="w-4 h-4" />
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
                <Download className="w-4 h-4 mr-2" />
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
                  <FileText className="w-5 h-5" />
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
                    {recording.transcript}
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
