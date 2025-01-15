package com.example.burhanqurickbot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private EditText etNewMessage;
    private Button btnSend;
    private ListView listView;
    private ChatAdapter chatAdapter;
    private ArrayList<ChatMessage> chatMessages;
    private final Handler handler = new Handler();
    private Handler animationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Set up the Admin button
        Button adminButton = findViewById(R.id.btnAdmin);
        adminButton.setOnClickListener(view -> {
            // Open AdminDashboardActivity
            Intent intent = new Intent(ChatActivity.this, AdminActivity.class);
            startActivity(intent);
        });

        // Initialize UI elements
        etNewMessage = findViewById(R.id.etNewMessage);
        btnSend = findViewById(R.id.btnSend);
        listView = findViewById(R.id.listView);

        // Initialize chat messages and adapter
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, chatMessages);
        listView.setAdapter(chatAdapter);

        // Add initial chat messages
        chatMessages.add(new ChatMessage("User", "Hello!"));
        chatMessages.add(new ChatMessage("Bot", "Hi, this is Burhan Qurik. How can I assist you?"));

        // Set up the send button click listener
        btnSend.setOnClickListener(view -> {
            String message = etNewMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                chatMessages.add(new ChatMessage("User", message));
                chatAdapter.notifyDataSetChanged();
                etNewMessage.setText("");

                ChatMessage typingMessage = new ChatMessage("Bot", "Typing...");
                chatMessages.add(typingMessage);
                chatAdapter.notifyDataSetChanged();
                listView.post(() -> listView.setSelection(chatAdapter.getCount() - 1));

                handler.postDelayed(() -> {
                    chatMessages.remove(typingMessage);
                    String reply = generateAutoReply(message);
                    chatMessages.add(new ChatMessage("Bot", reply));
                    chatAdapter.notifyDataSetChanged();
                    listView.post(() -> listView.setSelection(chatAdapter.getCount() - 1));
                }, 1500);
            } else {
                Toast.makeText(this, "Please type a message", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String generateAutoReply(String userMessage) {
        String[][] qaPairs = {
                {"hello", "Hello! How can I assist you today? Feel free to ask about fitness, nutrition, or health tips."},
                {"hi", "Hi! How can I assist you today? Feel free to ask about fitness, nutrition, or health tips."},
                {"hey","hey! How can I assist you today? Feel free to ask about fitness, nutrition, or health tips."},
                {"fitness", "Fitness is key to a healthy life. Consistency and a balanced approach are crucial."},
                {"gain weight", "To gain weight, focus on consuming more calories than you burn with a mix of healthy proteins, carbs, and fats."},
                {"warm up", "Warming up prepares your body for exercise by increasing blood flow, heart rate, and muscle temperature. Effective warm-ups include dynamic stretches, light jogging, or mobility exercises."},
                {"hydration", "Proper hydration supports muscle function, regulates body temperature, and enhances physical performance. Aim to drink water throughout the day and during workouts."},
                {"strength training", "Strength training involves exercises like lifting weights or bodyweight movements to build muscle, improve bone density, and boost metabolism."},
                {"stretching", "Stretching helps reduce muscle stiffness, improve flexibility, and enhance recovery. Dynamic stretching is ideal before workouts, while static stretching is better post-exercise."},
                {"goals", "Set SMART goals: Specific, Measurable, Achievable, Relevant, and Time-bound. For example, \"I’ll lose 5 pounds in 2 months by exercising 4 times a week and eating a balanced diet."},
                {"recovery", "Recovery includes proper rest, hydration, and nutrition to repair muscles and restore energy levels. Techniques like foam rolling and massages can also aid recovery."},
                {"balanced diet", "A balanced diet includes macronutrients (carbs, proteins, fats) and micronutrients (vitamins, minerals). Focus on whole, unprocessed foods for optimal nutrition."},
                {"sleep", "Quality sleep (7-9 hours) is essential for muscle repair, mental clarity, and hormonal balance. Create a bedtime routine and avoid screens before sleeping"},
                {"motivation", "Stay motivated by tracking progress, celebrating milestones, and reminding yourself of the reasons for your fitness journey. Surround yourself with supportive individuals."},
                {"posture", "Maintain good posture during exercises and daily activities to prevent strain and injuries."},
                {"yoga", "Yoga improves flexibility, strength, and mindfulness. It's great for both body and mind."},
                {"hydration tips", "Replace sugary drinks with water to stay hydrated and enhance workout performance."},
                {"mental health", "Engage in activities like walking or yoga to support your mental and physical health."},
                {"setbacks", "Don't let setbacks discourage you. Progress takes time, so keep moving forward."},
                {"long term health", "Focus on sustainable habits for long-term health rather than short-term results."},
                {"vegetables", "Eat a variety of colorful vegetables to get essential vitamins, minerals, and fiber."},
                {"processed foods", "Minimize processed food intake; choose whole, natural ingredients for better health."},
                {"progressive overload", "Progressive overload ensures continuous muscle growth and strength improvements."},
                {"late night meals", "Avoid heavy meals late at night to improve sleep and recovery."},
                {"patience", "Be patient and consistent. Fitness results take time but are worth the effort."},
                {"core exercises", "Strengthen your core for better posture, stability, and overall strength."},
                {"cardio benefits", "Cardio improves heart health, endurance, and helps in weight management."},
                {"calorie tracking", "Tracking calories helps align your intake with your fitness goals."},
                {"HIIT workouts", "High-Intensity Interval Training burns fat efficiently in shorter time frames."},
                {"outdoor activities", "Outdoor workouts boost mood and provide a fresh environment for exercise."},
                {"proper footwear", "Wearing the right shoes can prevent injuries and enhance workout performance."},
                {"protein intake", "Include protein-rich foods in your meals to aid muscle recovery and growth."},
                {"mindfulness", "Practice mindfulness to reduce stress and stay focused on your fitness journey."},
                {"mobility exercises", "Mobility exercises improve joint health and overall movement quality."},
                {"celebrate progress", "Acknowledge small victories to stay motivated and on track with your goals."},
                {"strength endurance", "Combine strength and endurance training for a balanced fitness approach."},
                {"habit building", "Focus on building one healthy habit at a time for lasting changes."},
                {"morning routines", "A morning fitness routine can set a positive tone for the rest of your day."},
                {"afternoon energy", "Afternoon workouts can boost energy and reduce stress after a busy morning."},
                {"evening relaxation", "Relaxing activities like yoga in the evening can help unwind and improve sleep."},
                {"overtraining", "Listen to your body to avoid overtraining and give yourself time to recover."},
                {"hydration timing", "Drink water consistently throughout the day for optimal hydration."},
                {"stretch duration", "Hold stretches for 20-30 seconds to improve flexibility effectively."},
                {"rest periods", "Take appropriate rest periods between sets for better performance and recovery."},
                {"balanced macros", "A mix of carbs, protein, and fats ensures proper energy and recovery."},
                {"fitness trackers", "Use fitness trackers to monitor your progress and stay motivated."},
                {"social support", "Workout with friends or join a community for motivation and accountability."},
                {"weekly goals", "Set weekly goals to stay consistent and measure your progress."},
                {"mental toughness", "Build mental toughness by embracing challenges and staying consistent."},
                {"positive habits", "Replace negative habits with positive ones for long-term benefits."},
                {"weight loss", "For effective weight loss, focus on a calorie deficit achieved through healthy eating and regular exercise."},
                {"muscle gain", "To build muscle, consume sufficient protein, follow a strength training program, and allow time for recovery."},
                {"calories", "Calories measure energy intake. Monitor your caloric intake to match your fitness goals."},
                {"fat loss", "Fat loss is best achieved by combining strength training, cardio, and a balanced diet in a calorie deficit."},
                {"carbs", "Carbohydrates are an essential energy source, especially for high-intensity workouts. Choose whole grains and fruits."},
                {"protein", "Protein is crucial for muscle repair and growth. Include lean meats, beans, and dairy in your diet."},
                {"healthy fats", "Healthy fats from sources like nuts, seeds, and avocados support hormone regulation and overall health."},
                {"intermittent fasting", "Intermittent fasting can help regulate calorie intake and improve metabolism, but it’s not for everyone."},
                {"meal prep", "Preparing meals in advance helps control portions, reduce unhealthy eating, and save time during the week."},
                {"cheat meals", "Occasional cheat meals can help curb cravings and keep you motivated, but moderation is key."},
                {"cardio workouts", "Cardio improves cardiovascular health and burns calories. Incorporate running, cycling, or swimming into your routine."},
                {"strength training tips", "Focus on proper form, progressive overload, and adequate rest for effective strength training."},
                {"flexibility", "Flexibility reduces injury risk and improves mobility. Include yoga or dynamic stretching in your routine."},
                {"water intake", "Drink at least 8-10 cups of water daily to stay hydrated, especially during exercise."},
                {"supplements", "Supplements like whey protein or multivitamins can support your diet, but focus on whole foods first."},
                {"pre workout meals", "Eat a light meal with carbs and protein 1-2 hours before exercising for energy."},
                {"post workout meals", "Consume protein and carbs after workouts to support recovery and muscle growth."},
                {"rest days", "Rest days are essential for muscle recovery, preventing burnout, and avoiding overtraining."},
                {"home workouts", "Bodyweight exercises like push-ups, squats, and planks are great for strength and convenience."},
                {"gym tips", "Start with a warm-up, focus on form, and follow a structured plan for efficient gym sessions."},
                {"weightlifting", "Weightlifting builds muscle, boosts metabolism, and strengthens bones. Gradually increase weights."},
                {"running tips", "Warm up before running, maintain proper form, and pace yourself to prevent injuries."},
                {"cycling", "Cycling is a low-impact cardio workout that improves endurance and strengthens the legs."},
                {"planks", "Planks are excellent for strengthening the core and improving stability and posture."},
                {"squats", "Squats work multiple muscle groups, improve leg strength, and enhance mobility."},
                {"push up", "Push-ups strengthen the chest, shoulders, and triceps. Modify as needed for your fitness level."},
                {"deadlifts", "Deadlifts are a compound exercise that strengthens the back, legs, and core. Focus on proper form."},
                {"bench press", "The bench press targets the chest, shoulders, and triceps. Use a spotter for safety."},
                {"pull up", "Pull-ups are great for building upper body strength, particularly in the back and biceps."},
                {"lunges", "Lunges improve leg strength, balance, and flexibility. Alternate legs for a balanced workout."},
                {"protein shakes", "Protein shakes are a convenient way to meet your protein needs, especially after workouts."},
                {"weight gain", "To gain weight, focus on a calorie surplus with nutrient-dense foods and strength training."},
                {"stress management", "Exercise, meditation, and proper sleep are effective ways to reduce stress."},
                {"core workouts", "Include exercises like planks, Russian twists, and leg raises to strengthen your core."},
                {"knee pain", "Strengthen supporting muscles and avoid high-impact exercises to manage knee pain effectively."},
                {"joint health", "Incorporate mobility exercises, stay hydrated, and consume omega-3s for joint health."},
                {"posture correction", "Strengthen your back and core while avoiding prolonged sitting to improve posture."},
                {"morning workouts", "Morning workouts boost energy, improve focus, and set a positive tone for the day."},
                {"evening workouts", "Evening workouts can help relieve stress and improve sleep quality."},
                {"foam rolling", "Foam rolling reduces muscle tightness, improves mobility, and aids recovery."},
                {"injury prevention", "Warm up properly, use correct form, and listen to your body to prevent injuries."},
                {"walking benefits", "Walking improves cardiovascular health, boosts mood, and aids weight management."},
                {"dietary fiber", "Fiber improves digestion, regulates blood sugar, and supports heart health. Include fruits, vegetables, and grains."},
                {"vitamins", "Vitamins are essential for overall health. Focus on a balanced diet to meet your needs."},
                {"mental clarity", "Regular exercise and proper hydration improve focus, memory, and cognitive function."},
                {"workout routines", "Follow a structured workout plan that aligns with your goals and fitness level."},
                {"energy levels", "Maintain stable energy levels with regular meals, hydration, and adequate sleep."},
                {"snacks", "Choose healthy snacks like nuts, fruits, or yogurt to maintain energy throughout the day."},
                {"weight plateaus", "Change up your routine and monitor your diet to overcome fitness plateaus."},
                {"aging and fitness", "Strength training, cardio, and flexibility exercises are crucial for healthy aging."},
                {"kids' fitness", "Encourage active play, sports, and fun exercises to promote kids' fitness."},
                {"family workouts", "Exercising with family can strengthen bonds and promote a healthy lifestyle."},
                {"holiday fitness", "Stay active during holidays with quick workouts and by making healthy food choices."},
                {"dehydration signs", "Signs include thirst, dark urine, fatigue, and headaches. Stay hydrated to avoid this."},
                {"low carb diets", "Low-carb diets may aid weight loss but aren’t suitable for everyone. Consult a nutritionist."},
                {"high protein diets", "High-protein diets support muscle repair but should include a balance of other nutrients."},
                {"cheat days", "Occasional indulgences are okay but maintain overall balance and moderation."},
                {"cardio vs strength training", "Cardio improves cardiovascular health and burns calories, while strength training builds muscle and boosts metabolism. Both are essential for a balanced fitness regimen."},
                {"importance of stretching", "Stretching enhances flexibility, prevents injuries, and promotes better recovery. Dynamic stretches are great for warm-ups, while static stretches are ideal for cool-downs."},
                {"importance of hydration", "Hydration regulates body temperature, lubricates joints, and aids in nutrient transport. Drink water consistently throughout the day and more during workouts."},
                {"high intensity interval training (HIIT)", "HIIT involves short bursts of intense activity followed by rest. It’s effective for burning calories, improving endurance, and saving time."},
                {"importance of sleep", "Sleep is crucial for recovery and overall health. Aim for 7-9 hours per night to allow your body to repair, grow, and maintain energy levels."},
                {"tracking fitness progress", "Monitor your progress using fitness apps, journals, or regular check-ins. Track workouts, diet, and measurements for motivation and adjustment."},
                {"best exercises for weight loss", "Focus on compound movements like squats, deadlifts, and burpees. Pair them with cardio activities like running or cycling for effective fat burn."},
                {"importance of protein", "Protein is essential for muscle repair and growth. Include lean meats, eggs, legumes, or plant-based sources to meet your daily requirements."},
                {"balanced diet tips", "A balanced diet includes a mix of carbs, proteins, fats, vitamins, and minerals. Prioritize whole foods like fruits, vegetables, lean proteins, and whole grains."},
                {"managing workout fatigue", "Combat fatigue by staying hydrated, eating nutrient-dense foods, and ensuring proper rest and recovery between sessions."},
                {"dealing with workout plateaus", "Switch up your routine, increase intensity, or try new exercises to challenge your body and break through plateaus."},
                {"importance of consistency", "Consistency in workouts and nutrition is the key to achieving and maintaining fitness goals. Small, consistent efforts yield long-term results."},
                {"mobility exercises", "Mobility exercises improve joint health, prevent injuries, and enhance range of motion. Examples include dynamic stretches and controlled movements like hip circles."},
                {"pre workout nutrition", "A meal rich in carbs and a bit of protein 1-2 hours before exercise can provide energy and optimize performance."},
                {"post workout nutrition", "Consuming protein and carbs within 30 minutes post-workout helps replenish glycogen stores and repair muscles."},
                {"benefits of walking", "Walking boosts cardiovascular health, aids weight management, improves mood, and is accessible for people of all fitness levels."},
                {"mental benefits of exercise", "Exercise reduces stress, anxiety, and depression while improving focus, memory, and overall mental clarity."},
                {"best core exercises", "Effective core exercises include planks, Russian twists, leg raises, and mountain climbers. They strengthen your core and improve stability."},
                {"importance of rest days", "Rest days allow your body to recover, repair muscles, and prevent burnout. Incorporate active recovery like light stretching or walking."},
                {"meal timing", "Eating at consistent intervals helps regulate blood sugar levels and energy. Space meals 3-4 hours apart, including snacks if needed."},
                {"healthy snacking ideas", "Opt for nutrient-dense snacks like Greek yogurt, nuts, fruit, or veggie sticks with hummus to fuel your day."},
                {"importance of warm-ups", "Warm-ups increase blood flow, prepare muscles for activity, and reduce injury risk. Include dynamic stretches or light cardio."},
                {"cool down techniques", "Cool-downs help lower your heart rate and reduce muscle soreness. Include static stretches and deep breathing exercises."},
                {"building muscle tips", "Focus on compound exercises, progressive overload, and a high-protein diet. Rest is equally crucial for muscle repair and growth."},
                {"low impact workouts", "Low-impact exercises like swimming, cycling, and yoga reduce joint stress while improving strength and endurance."},
                {"dealing with stress", "Incorporate exercise, mindfulness practices like meditation, and proper sleep to manage stress effectively."},
                {"importance of core strength", "A strong core enhances posture, balance, and overall functional strength, benefiting both daily life and athletic performance."},
                {"healthy aging tips", "Engage in regular strength and flexibility exercises, maintain a balanced diet, and prioritize sleep and stress management for healthy aging."},
                {"fitness for beginners", "Start with simple bodyweight exercises, focus on proper form, and gradually increase intensity. Consistency is more important than perfection."},
                {"protein for vegetarians", "Vegetarians can get protein from beans, lentils, tofu, tempeh, quinoa, nuts, and seeds. Include a variety to ensure complete amino acids."},
                {"effective weight loss tips", "Combine a calorie deficit with regular exercise, prioritize whole foods, and track your progress for sustainable weight loss."},
                {"importance of mindfulness", "Mindfulness improves focus, reduces stress, and enhances mental well-being. Combine it with yoga or meditation for best results."},
                {"cycling benefits", "Cycling improves cardiovascular fitness, strengthens legs, and is gentle on the joints. It’s also an eco-friendly transportation method."},
                {"stretching after workouts", "Stretching after workouts helps relax muscles, improve flexibility, and reduce post-exercise soreness."},
                {"benefits of yoga", "Yoga improves flexibility, balance, and strength while promoting relaxation and stress relief. It’s suitable for all fitness levels."},
                {"dealing with sore muscles", "Alleviate soreness with light stretching, hydration, foam rolling, and ensuring adequate protein intake for muscle repair."},
                {"importance of variety in workouts", "Varied workouts prevent boredom, reduce plateaus, and target different muscle groups for overall fitness improvement."},
                {"reasons for overtraining", "Overtraining occurs due to insufficient rest, excessive workouts, or inadequate nutrition. Listen to your body to avoid burnout."},
                {"importance of mental health", "Exercise improves mental health by reducing stress hormones, releasing endorphins, and boosting overall mood and confidence."},
                {"building workout habits", "Start small, set realistic goals, and establish a routine that fits your schedule. Consistency turns habits into lifestyle changes."},
                {"best cardio exercises", "Effective cardio exercises include running, cycling, swimming, and jump rope. Choose what you enjoy to stay consistent."},
                {"healthy eating for families", "Plan balanced meals with lean proteins, whole grains, and vegetables. Involve the family in meal prep to encourage healthy habits."},
                {"fasting benefits", "Intermittent fasting may aid in weight management and metabolic health but consult a doctor before starting, especially with medical conditions."},
                {"lose weight", "To lose weight, focus on a calorie deficit by eating nutrient-dense foods, exercising regularly, and maintaining consistent hydration. Avoid crash diets as they aren't sustainable."},
                {"best diet for weight loss", "The best diet is one that includes whole foods, lean proteins, healthy fats, and complex carbs. Focus on portion control and avoid processed foods."},
                {"calorie deficit", "A calorie deficit is consuming fewer calories than you burn. This encourages your body to use stored fat for energy, leading to weight loss."},
                {"exercise for fat loss", "Combine cardio (like running or swimming) and strength training to burn calories and preserve muscle during fat loss."},
                {"intermittent fasting", "Intermittent fasting involves cycling between eating and fasting periods. It can help regulate calorie intake and improve metabolic health."},
                {"weight loss plateau", "To overcome a plateau, adjust your diet, increase workout intensity, or try new exercises to challenge your body."},
                {"meal prep for weight loss", "Prepare meals in advance to control portions, reduce unhealthy snacking, and stick to your calorie goals."},
                {"importance of protein in weight loss", "Protein keeps you fuller longer, boosts metabolism, and helps preserve muscle mass during weight loss."},
                {"HIIT for weight loss", "High-Intensity Interval Training burns calories quickly and improves cardiovascular health in short sessions."},
                {"low-carb diets", "Low-carb diets reduce insulin levels, helping the body use fat for energy. Include lean proteins, healthy fats, and low-carb vegetables."},
                {"cheat meals", "Occasional cheat meals can boost metabolism and provide a psychological break. Keep portions moderate to avoid setbacks."},
                {"portion control", "Use smaller plates, measure servings, and eat mindfully to avoid overeating and maintain calorie control."},
                {"importance of water in weight loss", "Drinking water increases metabolism, reduces appetite, and helps the body efficiently burn calories."},
                {"how to stay motivated", "Set clear goals, track progress, celebrate small wins, and find an accountability partner to stay motivated."},
                {"walking for weight loss", "Walking is a low-impact exercise that burns calories, improves mood, and is easy to incorporate into daily routines."},
                {"tracking calories", "Use apps or journals to track your caloric intake and ensure you're meeting your weight loss goals."},
                {"healthy snacking for weight loss", "Opt for snacks like fruits, nuts, or Greek yogurt, which are nutrient-dense and low in empty calories."},
                {"importance of sleep in weight loss", "Poor sleep disrupts hunger hormones and increases cravings, making it harder to stick to a weight loss plan."},
                {"stress and weight loss", "Chronic stress can lead to emotional eating and weight gain. Practice stress management techniques like yoga or meditation."},
                {"foods to avoid for weight loss", "Avoid sugary drinks, fried foods, processed snacks, and refined carbs as they are high in calories and low in nutrients."},
                {"building sustainable habits", "Focus on small, consistent changes like meal planning, regular exercise, and mindful eating for long-term success."},
                {"metabolism and weight loss", "Boost metabolism with strength training, eating protein-rich foods, staying hydrated, and getting enough sleep."},
                {"importance of fiber", "Fiber promotes satiety, improves digestion, and helps regulate blood sugar levels, aiding in weight loss."},
                {"emotional eating", "Identify triggers, practice mindfulness, and find alternative stress-relief activities like journaling or exercise to curb emotional eating."},
                {"breaking bad eating habits", "Replace unhealthy habits with better ones, like swapping sugary drinks for water and preparing meals at home."},
                {"role of healthy fats", "Healthy fats like avocados, nuts, and olive oil provide energy, support brain health, and improve satiety, aiding in weight management."},
                {"importance of meal timing", "Eating meals at regular intervals stabilizes blood sugar and prevents overeating, supporting weight loss."},
                {"vegetarian weight loss tips", "Focus on plant-based proteins like lentils, chickpeas, and tofu. Combine with whole grains and veggies for balanced meals."},
                {"workout consistency", "Stick to a workout routine by scheduling exercise at the same time daily and choosing activities you enjoy."},
                {"strength training for weight loss", "Strength training builds muscle, which increases resting metabolic rate and burns calories even at rest."},
                {"best cardio for fat loss", "Running, cycling, swimming, and jump rope are effective cardio exercises for burning calories and fat."},
                {"importance of self-discipline", "Self-discipline ensures you stay committed to your fitness goals, even when motivation wanes."},
                {"detox diets", "Most detox diets lack scientific support. Instead, focus on whole foods, hydration, and exercise for natural detoxification."},
                {"importance of breakfast", "A nutritious breakfast jumpstarts metabolism, prevents overeating later, and provides energy for the day."},
                {"avoiding sugar cravings", "Combat sugar cravings by eating balanced meals, staying hydrated, and opting for naturally sweet options like fruits."},
                {"setting realistic goals", "Set achievable, specific goals and break them into smaller milestones to maintain motivation and track progress."},
                {"benefits of mindfulness", "Mindfulness improves focus, reduces stress, and helps prevent overeating by fostering a connection with your body's hunger cues."},
                {"time management for workouts", "Prioritize workouts by scheduling them into your day and choosing efficient routines like HIIT."},
                {"importance of meal diversity", "Varied meals provide a range of nutrients, prevent boredom, and promote adherence to a healthy eating plan."},
                {"role of vitamins and minerals", "Micronutrients like Vitamin D, calcium, and iron are essential for overall health and support energy levels during weight loss."},
                {"importance of accountability", "Share your goals with a friend, join a fitness group, or use an app to stay accountable and motivated."},
                {"impact of alcohol on weight loss", "Alcohol contains empty calories and can hinder fat metabolism. Limit intake to stay on track with weight loss goals."},
                {"benefits of meal planning", "Meal planning saves time, ensures portion control, and reduces reliance on unhealthy convenience foods."},
                {"importance of core strength", "Core exercises improve posture, stability, and overall physical performance, supporting other weight loss activities."},
                {"tracking progress with photos", "Progress photos provide a visual record of changes, which can be more motivating than scale weight alone."},
                {"role of mindfulness in eating", "Mindful eating helps you enjoy food, recognize hunger and fullness cues, and avoid overeating."},
                {"gain weight", "To gain weight, focus on consuming calorie-dense, nutrient-rich foods like nuts, avocados, whole grains, and lean proteins. Combine this with strength training to build muscle."},
                {"high-calorie foods for weight gain", "Include foods like nut butters, full-fat dairy, olive oil, and dried fruits in your diet for a calorie boost."},
                {"how to eat more calories", "Eat smaller, more frequent meals, include snacks, and add healthy oils or toppings like cheese to meals."},
                {"protein for weight gain", "Consume protein-rich foods like chicken, fish, eggs, and legumes to support muscle growth and repair."},
                {"healthy weight gain strategies", "Focus on whole foods, avoid junk calories, and balance macronutrients with healthy fats, proteins, and carbs."},
                {"importance of strength training for weight gain", "Strength training helps build muscle, increasing overall weight in a healthy way rather than gaining fat."},
                {"how many meals for weight gain", "Aim for 5-6 smaller meals per day to increase your calorie intake without feeling overly full."},
                {"weight gain shakes", "Create shakes using ingredients like whole milk, bananas, peanut butter, and protein powder to increase calorie intake."},
                {"how to increase appetite", "Stimulate your appetite by exercising regularly, drinking water before meals, and adding spices to your dishes."},
                {"gaining weight with a fast metabolism", "Choose calorie-dense foods, eat more frequently, and focus on resistance training to overcome a fast metabolism."},
                {"foods to avoid for healthy weight gain", "Avoid sugary snacks and junk food. Opt for nutrient-dense, whole foods to gain weight healthily."},
                {"importance of carbs for weight gain", "Carbohydrates provide energy and help replenish glycogen stores, supporting muscle growth and weight gain."},
                {"role of fats in weight gain", "Healthy fats are calorie-dense and essential for hormone production. Include avocados, nuts, seeds, and oils in your diet."},
                {"caloric surplus for weight gain", "A caloric surplus involves consuming more calories than your body burns. Track your intake to ensure you're meeting your goals."},
                {"post-workout nutrition for weight gain", "Eat a combination of protein and carbs after a workout to support muscle recovery and growth."},
                {"importance of sleep for weight gain", "Sleep is critical for muscle recovery and growth. Aim for 7-9 hours per night to support healthy weight gain."},
                {"weight gain for skinny people", "Skinny individuals should focus on eating calorie-dense foods, strength training, and maintaining consistency in their eating habits."},
                {"how to track progress for weight gain", "Use a combination of scale weight, body measurements, and progress photos to track your weight gain journey."},
                {"importance of consistency in weight gain", "Consistently eating and training according to your plan ensures gradual and sustainable weight gain over time."},
                {"snacks for weight gain", "Choose snacks like trail mix, granola bars, Greek yogurt with honey, and boiled eggs to increase calorie intake."},
                {"importance of liquid calories", "Drinking high-calorie beverages like smoothies, milk, and meal replacement shakes is an easy way to increase calorie intake."},
                {"role of compound exercises", "Compound exercises like squats, deadlifts, and bench presses stimulate muscle growth, aiding in healthy weight gain."},
                {"how to avoid fat gain while gaining weight", "Focus on strength training, monitor your caloric surplus, and prioritize protein to build muscle rather than fat."},
                {"gaining weight for athletes", "Athletes should focus on eating nutrient-dense foods, incorporating supplements like protein powder, and maintaining proper recovery."},
                {"importance of micronutrients in weight gain", "Micronutrients like vitamins and minerals are essential for overall health and play a supportive role in muscle growth and recovery."},
                {"weight gain with vegetarian diet", "Include plant-based protein sources like lentils, tofu, quinoa, and nuts to gain weight on a vegetarian diet."},
                {"how to manage portion sizes for weight gain", "Increase portion sizes gradually, adding more calories per meal without overwhelming your appetite."},
                {"weight gain for women", "Women should focus on strength training and eating nutrient-dense foods to gain weight healthily while maintaining overall fitness."},
                {"meal prep for weight gain", "Prepare high-calorie meals in advance to ensure you always have access to nutrient-rich options."},
                {"importance of hydration for weight gain", "Staying hydrated supports digestion and nutrient absorption, which are vital for healthy weight gain."},
                {"weight gain supplements", "Consider supplements like mass gainers, whey protein, and creatine to complement a high-calorie diet."},
                {"building muscle and gaining weight", "Focus on progressive overload in your workouts and consume a protein-rich diet to build muscle mass."},
                {"how to reduce bloating while gaining weight", "Avoid overeating in one sitting, eat slowly, and include foods that support digestion like yogurt and ginger."},
                {"best exercises for weight gain", "Focus on compound movements like squats, deadlifts, pull-ups, and bench presses to stimulate muscle growth."},
                {"importance of digestion in weight gain", "Ensure proper digestion by eating fiber-rich foods and staying hydrated to maximize nutrient absorption."},
                {"how to gain weight after illness", "Gradually reintroduce nutrient-dense foods and focus on light exercises to rebuild strength and weight."},
                {"gaining weight without gym", "Use bodyweight exercises like push-ups, pull-ups, and resistance bands to build muscle at home while eating a high-calorie diet."},
                {"importance of patience in weight gain", "Weight gain takes time and consistency. Avoid rushing the process to ensure long-term, sustainable results."},
                {"mental health and weight gain", "Address stress or emotional barriers to eating and consult a therapist if mental health challenges are affecting your weight gain goals."},
                {"hello, hi, greetings, hey", "Hello! How can I assist you today? Feel free to ask about fitness, nutrition, or health tips."},
                {"gain weight, weight gain tips, weight gain strategies", "To gain weight, focus on consuming calorie-dense, nutrient-rich foods like nuts, avocados, whole grains, and lean proteins. Combine this with strength training to build muscle."},
                {"weight loss, lose weight, fat loss tips", "For weight loss, create a calorie deficit by eating smaller portions, focusing on whole foods, and incorporating regular physical activity like cardio and strength training."},
                {"hydration, drink water, benefits of hydration", "Staying hydrated helps with digestion, nutrient absorption, and overall energy levels. Aim for at least 8 glasses of water daily, and more if you're active."},
                {"healthy eating, balanced diet, nutrition tips", "A balanced diet includes lean proteins, whole grains, healthy fats, and a variety of fruits and vegetables. Avoid processed foods and added sugars."},
                {"cardio, aerobic exercise, running benefits", "Cardio exercises like running, cycling, or swimming improve heart health, burn calories, and boost endurance. Aim for 150 minutes of moderate-intensity activity weekly."},
                {"protein intake, protein foods, how much protein", "Include protein in every meal with sources like chicken, fish, tofu, eggs, and beans. Aim for 1.2–2.0 grams of protein per kilogram of body weight for fitness goals."},
                {"calorie surplus, eating more calories, gain weight diet", "To gain weight, eat more calories than you burn. Add snacks, use healthy oils, and include calorie-dense foods like nuts and avocados."},
                {"strength training, lifting weights, building muscle", "Strength training is essential for building muscle. Use compound movements like squats, deadlifts, and bench presses, and progressively increase weight."},
                {"HIIT, high-intensity interval training, burn fat fast", "HIIT involves short bursts of intense exercise followed by rest. It’s effective for fat loss and improves cardiovascular fitness in less time than steady-state cardio."},
                {"core workouts, abs exercises, strengthen core", "Strengthen your core with exercises like planks, Russian twists, and leg raises. A strong core improves balance, posture, and overall fitness."},
                {"mobility, flexibility, stretching routines", "Incorporate stretching and mobility exercises into your routine to improve flexibility, reduce stiffness, and prevent injuries."},
                {"meal planning, how to meal prep, weekly meal plans", "Plan your meals ahead to save time and stick to your goals. Include balanced portions of protein, carbs, and fats for each meal."},
                {"calorie tracking, food logs, monitor intake", "Use apps or journals to track your food intake and ensure you’re meeting your calorie and nutrient goals."},
                {"mental health, stress relief, relaxation techniques", "For better mental health, practice mindfulness, meditate, exercise, and ensure you get enough sleep to manage stress."},
                {"post-workout meals, recovery nutrition, what to eat after exercise", "Post-workout meals should include protein and carbs to aid recovery and muscle repair. Examples include a chicken sandwich or a protein shake with fruit."},
                {"home workouts, no gym exercise, bodyweight exercises", "You can stay fit at home with bodyweight exercises like push-ups, squats, and planks. Add resistance bands for more variety."},
                {"motivation, staying consistent, how to stay motivated", "Set realistic goals, track your progress, and celebrate small wins. Surround yourself with supportive people or find a workout buddy."},
                {"sleep quality, better sleep, rest for recovery", "Aim for 7–9 hours of quality sleep per night. Stick to a routine, avoid screens before bedtime, and create a comfortable sleeping environment."},
                {"weight gain shakes, high-calorie drinks, gain weight smoothies", "Create shakes with ingredients like whole milk, bananas, peanut butter, and protein powder for a quick calorie boost."},
                {"exercise for beginners, starting workouts, beginner fitness tips", "Begin with light exercises like walking or yoga, and gradually increase intensity. Focus on proper form to avoid injuries."},
                {"diet for muscle gain, muscle-building diet, protein for muscles", "To build muscle, focus on protein-rich foods like chicken, eggs, fish, beans, and dairy. Include complex carbs like oats and sweet potatoes for energy."},
                {"fast recovery tips, post-workout recovery, recovery after exercise", "To recover quickly, hydrate, stretch, and eat a balanced post-workout meal. Include protein and carbs to repair muscles and replenish energy."},
                {"importance of warm-up, warming up exercises, pre-workout routine", "Warming up prepares your body for exercise by increasing blood flow and reducing injury risk. Try dynamic stretches or light cardio for 5–10 minutes."},
                {"cool-down exercises, stretching after workout, post-workout cool-down", "Cool-down exercises like light jogging and stretching help reduce muscle soreness and improve flexibility after a workout."},
                {"how to gain stamina, increase endurance, improve stamina", "Build stamina with regular aerobic activities like running or cycling. Gradually increase intensity and duration while maintaining a balanced diet."},
                {"fitness for seniors, elder exercises, workouts for older adults", "Seniors should focus on low-impact exercises like walking, swimming, or yoga. Strength training with light weights can help maintain muscle and bone health."},
                {"how to reduce belly fat, losing abdominal fat, trim waistline", "To reduce belly fat, focus on a calorie deficit, eat high-fiber foods, and include exercises like HIIT and core strengthening."},
                {"exercise for mental health, workouts for stress relief, mood-boosting activities", "Physical activities like yoga, walking, or dancing can reduce stress and boost mood by releasing endorphins."},
                {"how to get six-pack abs, abs workout plan, achieving a flat stomach", "Combine core exercises like planks and crunches with a calorie deficit diet to reduce body fat and reveal abs."},
                {"cardio or strength, which is better, cardio vs strength training", "Both are important. Cardio improves heart health and burns calories, while strength training builds muscle and boosts metabolism."},
                {"how to avoid injuries, exercise safety tips, injury prevention", "Use proper form, warm up, cool down, and avoid overtraining. Gradually increase workout intensity and rest when needed."},
                {"healthy snacks, what to eat between meals, nutritious snacking", "Choose snacks like fruits, nuts, Greek yogurt, or whole-grain crackers to stay energized and avoid overeating."},
                {"weight loss plateau, why no weight loss, breaking plateaus", "If weight loss stalls, try adjusting your diet, increasing exercise intensity, or changing your workout routine to challenge your body."},
                {"exercise for busy people, short workouts, staying active with a busy schedule", "Incorporate quick workouts like HIIT or bodyweight exercises. Stay active by taking stairs, walking, or stretching during breaks."},
                {"best time to exercise, morning or evening workouts, timing workouts", "The best time to exercise depends on your schedule and energy levels. Morning workouts may boost focus, while evening sessions can help with stress relief."},
                {"intermittent fasting, does it work, fasting for weight loss", "Intermittent fasting involves eating within specific time windows. It can aid weight loss if combined with healthy food choices and portion control."},
                {"vegan fitness, plant-based diet for fitness, vegan protein sources", "Vegan diets can support fitness with foods like lentils, chickpeas, tofu, quinoa, and nuts. Ensure adequate protein intake and supplement B12 if needed."},
                {"importance of core strength, benefits of core exercises, strong core advantages", "A strong core improves posture, balance, and performance in almost all physical activities. Incorporate exercises like planks and Russian twists."},
                {"home workout equipment, affordable fitness gear, exercise at home", "Get dumbbells, resistance bands, a yoga mat, and a stability ball for versatile home workouts without taking up much space."},
                {"workout for kids, fitness for children, active kids tips", "Encourage kids to stay active with fun activities like sports, swimming, or dancing. Limit screen time and promote outdoor play."},
                {"pregnancy fitness, safe exercises for pregnancy, staying active during pregnancy", "Pregnant women can benefit from low-impact activities like walking, prenatal yoga, and swimming. Always consult a doctor before starting any routine."},
                {"postnatal fitness, workouts after childbirth, recovery after pregnancy", "Focus on gentle exercises like walking and pelvic floor strengthening. Gradually reintroduce strength training after consulting a healthcare professional."},
                {"joint health, exercises for joints, arthritis-friendly workouts", "Low-impact exercises like swimming, cycling, and yoga are gentle on joints and can improve mobility and reduce stiffness."},
                {"how to stay consistent, fitness consistency, build workout habits", "Schedule workouts, set reminders, and create a routine you enjoy. Start small and gradually build up to avoid burnout."},
                {"importance of stretching, benefits of flexibility, why stretch", "Stretching improves flexibility, reduces stiffness, and helps prevent injuries. Include dynamic stretches before workouts and static stretches afterward."},
                {"how to increase metabolism, boosting metabolism, tips for faster metabolism", "Increase metabolism by building muscle, staying active, eating protein-rich meals, and staying hydrated throughout the day."},
                {"low-carb diets, benefits of low-carb, keto diet tips", "Low-carb diets like keto focus on reducing carbs and increasing healthy fats. They can help with weight loss and improving insulin sensitivity."},
                {"importance of rest days, why rest is crucial, recovery days", "Rest days allow muscles to repair and grow, prevent overtraining, and reduce injury risk. Listen to your body and take breaks when needed."},
                {"how to stay motivated, overcoming laziness, building discipline", "Set achievable goals, track your progress, and remind yourself of the benefits. Surround yourself with supportive people and reward small milestones."},
                {"how to improve posture, posture correction, exercises for posture", "Improve posture with exercises like planks, rows, and stretches for your chest and shoulders. Sit and stand with a straight back and shoulders down."},
                {"importance of hydration, staying hydrated, drinking water benefits", "Hydration helps maintain energy levels, supports digestion, and regulates body temperature. Drink at least 8-10 glasses of water daily."},
                {"how to avoid sugar cravings, managing cravings, tips for reducing sugar", "Combat sugar cravings by eating balanced meals, drinking water, and consuming natural sweeteners like fruits. Avoid skipping meals to stabilize blood sugar."},
                {"exercise for beginners, starting a fitness journey, first-time workouts", "Start with light activities like walking or yoga. Gradually incorporate strength training and cardio as your stamina improves. Consistency is key."},
                {"importance of protein, high-protein diets, protein intake for fitness", "Protein helps repair muscles, supports weight loss, and keeps you full. Include lean meats, eggs, beans, and dairy in your diet."},
                {"benefits of yoga, yoga for fitness, mental health yoga", "Yoga enhances flexibility, reduces stress, and improves strength. Practice regularly for both physical and mental health benefits."},
                {"what is BMI, calculate BMI, understanding body mass index", "BMI is a measure of body fat based on height and weight. Use it as a general guide, but consider muscle mass and overall health as well."},
                {"how to improve balance, exercises for stability, stability workouts", "Improve balance with exercises like single-leg stands, yoga poses, and core-strengthening activities like planks and bridges."},
                {"meal prep tips, easy meal planning, benefits of meal prep", "Meal prep saves time and ensures you stick to healthy eating. Plan your meals, cook in bulk, and store them in portioned containers."},
                {"importance of vitamins, essential nutrients, best vitamins for health", "Vitamins support immunity, energy, and overall well-being. Eat a variety of fruits, vegetables, and whole foods to meet your needs."},
                {"how to boost energy, fighting fatigue, staying energetic", "Boost energy by staying hydrated, eating balanced meals, and exercising regularly. Avoid processed foods and get enough sleep."},
                {"how to reduce stress, stress management techniques, calming exercises", "Manage stress with deep breathing, meditation, and physical activities like walking or yoga. Maintain a balanced routine and seek support when needed."},
                {"workouts for women, fitness tips for females, women-specific exercises", "Women can benefit from strength training, cardio, and flexibility exercises. Tailor your routine to your goals, such as toning or endurance."},
                {"importance of breakfast, best breakfast options, skipping breakfast effects", "A healthy breakfast kickstarts your metabolism and provides energy. Choose options like oatmeal, eggs, or smoothies for a balanced start."},
                {"how to improve digestion, gut health tips, better digestion foods", "Improve digestion with fiber-rich foods, staying hydrated, and eating probiotic-rich options like yogurt or kimchi. Chew your food well."},
                {"how to stay active in winter, winter fitness tips, cold weather workouts", "Stay active in winter with indoor exercises like yoga or weight training. Dress warmly for outdoor activities and stay consistent with your routine."},
                {"importance of fiber, fiber-rich foods, benefits of dietary fiber", "Fiber aids digestion, supports weight loss, and lowers cholesterol. Include fruits, vegetables, whole grains, and legumes in your diet."},
                {"benefits of cycling, cycling for fitness, why cycle", "Cycling improves cardiovascular health, builds leg strength, and is a low-impact exercise that's easy on the joints. Great for all fitness levels."},
                {"how to relieve muscle soreness, soreness remedies, post-workout pain tips", "Relieve muscle soreness with light stretching, foam rolling, and staying hydrated. Consider warm baths or massage for added relief."},
                {"how to improve sleep, better sleep tips, sleep hygiene", "Improve sleep by maintaining a consistent bedtime, avoiding screens before bed, and creating a relaxing nighttime routine. Limit caffeine in the evening."},
                {"how to increase flexibility, flexibility exercises, stretching tips", "Increase flexibility with daily stretches like hamstring stretches and yoga poses. Gradual and consistent practice is key."},
                {"exercises for abs, core workouts, six-pack training", "Focus on exercises like planks, crunches, and Russian twists. Combine with cardio and a clean diet to reveal abdominal muscles."},
                {"how to reduce stress eating, avoid emotional eating, healthy stress coping", "Manage stress eating by identifying triggers, practicing mindful eating, and keeping healthy snacks available. Engage in stress-reducing activities."},
                {"how to build confidence, self-esteem tips, confidence through fitness", "Building confidence comes with achieving small fitness goals, improving posture, and practicing positive self-talk. Celebrate your progress."},
                {"how to reduce back pain, back pain exercises, tips for a healthy back", "Reduce back pain with exercises like cat-cow stretches, planks, and swimming. Maintain good posture and avoid heavy lifting."},
                {"importance of rest, sleep benefits, recovery for health", "Rest supports recovery, mental health, and energy levels. Aim for 7-9 hours of quality sleep and take breaks during the day when needed."},
                {"best cardio exercises, cardio for beginners, effective cardio workouts", "Try running, swimming, cycling, or dancing for effective cardio. Start with 20-30 minutes and increase intensity as your fitness improves."},
                {"how to track fitness progress, monitoring goals, fitness tracking tips", "Track progress with fitness apps, journaling, or wearable devices. Monitor weight, strength, endurance, and overall well-being."},
                {"exercises for arms, arm strength workouts, toned arms tips", "Strengthen arms with exercises like bicep curls, tricep dips, and push-ups. Use resistance bands or weights to increase intensity."},
                {"importance of heart health, cardiovascular fitness, heart-healthy activities", "Improve heart health with regular cardio, a balanced diet, and reducing stress. Avoid smoking and limit processed foods."},
                {"how to manage time for fitness, scheduling workouts, busy life exercise tips", "Prioritize short workouts, combine cardio with strength, and stay active throughout the day with small changes like walking or stretching."},
                {"importance of sunlight, vitamin D benefits, outdoor activities", "Sunlight boosts vitamin D, supports mood, and strengthens bones. Spend 10-30 minutes outside daily while protecting your skin with sunscreen."},


        };

        for (String[] pair : qaPairs) {
            if (userMessage.toLowerCase().contains(pair[0].toLowerCase())) {
                return pair[1];
            }
        }
        return "I'm sorry, I didn't understand that. Could you please rephrase?";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
