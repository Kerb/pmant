import {motion} from "framer-motion";
import {AppLayout} from "@/components/layout/AppLayout";
import {StatsCard} from "@/components/dashboard/StatsCard";
import {RecentMeetings} from "@/components/dashboard/RecentMeetings";
import {Calendar, FileText, Clock, TrendingUp} from "lucide-react";
import {useEffect, useState} from "react";

export default function Dashboard() {

    const [userSession, setUserSession] = useState(null);

    useEffect(() => {
        // Fetch user session data from backend
        fetch('/api/me', {
            credentials: 'include'
        })
        .then(response => response.json())
        .then(data => {
            console.log('User session data:', data);
            // Here you can set the data to state if needed
            setUserSession(data);
        })
        .catch(error => {
            console.error('Error fetching session data:', error);
        });
    }, []);

    return (
        <AppLayout title="Dashboard">
            <div className="space-y-8">
                {/* Welcome Section */}
                <motion.div
                    initial={{y: 20, opacity: 0}}
                    animate={{y: 0, opacity: 1}}
                    transition={{duration: 0.4}}
                    className="space-y-2"
                >
                    <h2 className="text-3xl font-bold text-foreground">
                        Welcome back, {userSession ? userSession.login : 'Guest'} 👋
                    </h2>
                    <p className="text-muted-foreground">
                        Here's what's happening with your meetings today.
                    </p>
                </motion.div>

                {/* Stats Cards */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                    <StatsCard
                        title="Meetings This Week"
                        value={12}
                        icon={Calendar}
                        trend="+2 from last week"
                        delay={0.1}
                    />
                    <StatsCard
                        title="Total Transcripts"
                        value={148}
                        icon={FileText}
                        trend="+15 this month"
                        delay={0.15}
                    />
                    <StatsCard
                        title="Avg Duration"
                        value="42 min"
                        icon={Clock}
                        trend="Optimal range"
                        delay={0.2}
                    />
                    <StatsCard
                        title="Productivity"
                        value="94%"
                        icon={TrendingUp}
                        trend="+5% improvement"
                        delay={0.25}
                    />
                </div>

                {/* Recent Meetings Table */}
                <RecentMeetings/>
            </div>
        </AppLayout>
    );
}
