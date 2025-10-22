import { motion } from "framer-motion";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Calendar, Clock, Users } from "lucide-react";

const meetings = [
  {
    id: 1,
    title: "Product Strategy Meeting",
    date: "2025-01-20",
    time: "10:00 AM",
    duration: "45 min",
    participants: 8,
    status: "Completed",
  },
  {
    id: 2,
    title: "Design Review",
    date: "2025-01-19",
    time: "2:30 PM",
    duration: "30 min",
    participants: 5,
    status: "Completed",
  },
  {
    id: 3,
    title: "Client Call - Q1 Planning",
    date: "2025-01-18",
    time: "11:00 AM",
    duration: "60 min",
    participants: 12,
    status: "Completed",
  },
  {
    id: 4,
    title: "Team Standup",
    date: "2025-01-18",
    time: "9:00 AM",
    duration: "15 min",
    participants: 10,
    status: "Completed",
  },
];

export function RecentMeetings() {
  return (
    <motion.div
      initial={{ y: 20, opacity: 0 }}
      animate={{ y: 0, opacity: 1 }}
      transition={{ delay: 0.4, duration: 0.4 }}
    >
      <Card className="border-border shadow-md">
        <CardHeader>
          <CardTitle className="text-xl font-bold">Recent Meetings</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {meetings.map((meeting, index) => (
              <motion.div
                key={meeting.id}
                initial={{ x: -20, opacity: 0 }}
                animate={{ x: 0, opacity: 1 }}
                transition={{ delay: 0.5 + index * 0.1, duration: 0.3 }}
                whileHover={{ x: 4, transition: { duration: 0.2 } }}
                className="flex items-center justify-between p-4 rounded-2xl border border-border hover:border-primary/20 hover:bg-accent/5 transition-all duration-200 cursor-pointer"
              >
                <div className="flex-1 space-y-2">
                  <h3 className="font-semibold text-foreground">{meeting.title}</h3>
                  <div className="flex items-center gap-4 text-sm text-muted-foreground">
                    <div className="flex items-center gap-1">
                      <Calendar className="h-4 w-4" />
                      <span>{meeting.date}</span>
                    </div>
                    <div className="flex items-center gap-1">
                      <Clock className="h-4 w-4" />
                      <span>{meeting.time}</span>
                    </div>
                    <div className="flex items-center gap-1">
                      <Users className="h-4 w-4" />
                      <span>{meeting.participants} participants</span>
                    </div>
                  </div>
                </div>
                <div className="flex items-center gap-3">
                  <Badge variant="secondary" className="bg-secondary text-secondary-foreground">
                    {meeting.duration}
                  </Badge>
                  <Badge className="bg-gradient-primary text-white border-0">
                    {meeting.status}
                  </Badge>
                </div>
              </motion.div>
            ))}
          </div>
        </CardContent>
      </Card>
    </motion.div>
  );
}
